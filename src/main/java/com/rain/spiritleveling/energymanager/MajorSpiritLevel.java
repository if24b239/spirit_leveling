package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.MinorSpiritLevelFactory;

import java.util.ArrayList;

public class MajorSpiritLevel<minorLevelType extends MinorSpiritLevel> {
    private final MinorSpiritLevelFactory<minorLevelType> factory;

    private int spiritLevel;
    private boolean isComplete = false;

    ArrayList<minorLevelType> levels = new ArrayList<>();

    public MajorSpiritLevel(int s_level, int max_energy, boolean minorBottleneck, MinorSpiritLevelFactory<minorLevelType> factory) {

        //initialize the factory
        this.factory = factory;

        spiritLevel = s_level;

        createMinorLevels(s_level);

        // update minor levels to fit with data given in constructor
        int level_size = MinorSpiritLevel.getLevelSize(s_level);
        int completed_minor_levels = max_energy / level_size;
        int progress_next = max_energy % level_size;

        for (int i = 0; i < completed_minor_levels; i++) {
            levels.get(i).setToComplete();
            levels.get(i).minorBreakthrough();
        }

        if (minorBottleneck) return;

        if (completed_minor_levels < 10) {
            // set breakthrough state correctly
            levels.get(completed_minor_levels).minorBreakthrough();

            // set progress to correct state
            levels.get(completed_minor_levels).addProgress(progress_next);
        }
    }

    // returns the number of completed minor levels which equals either the index of the next not completed minorLevel or 10
    public int getMinorLevel() {
        int completedMinorLevels = 0;
        for (MinorSpiritLevel l : levels) {
            if (l.getIsComplete()) completedMinorLevels++;
        }

        return completedMinorLevels;
    }

    // adds progress and returns the amount of spirit energy that can be added
    protected int addProgress(int amount) {
        if (isComplete) return 0;

        int minorLevel = getMinorLevel();

        // add the amount to the current minor level
        int newAmount = amount;
        if (minorLevel < 10)
            newAmount = levels.get(minorLevel).addProgress(amount);

        // set the major level to complete if after adding the progress all 10 minor levels are complete
        if (getMinorLevel() >= 10) isComplete = true;

        return newAmount;
    }

    // increases spiritLevel and remakes the minor levels as long as the current major level is complete
    public boolean majorBreakthrough() {
        if (!isComplete) return false;

        spiritLevel++;

        levels.clear();
        createMinorLevels(spiritLevel);

        isComplete = false;

        // unlock the second minor level of the new spirit level
        minorBreakthrough(spiritLevel);

        return true;
    }

    // will remove chains off the next minor level
    public boolean minorBreakthrough(int breakthroughLevel) {
        if (isComplete || breakthroughLevel != spiritLevel) return false;

        return levels.get(getMinorLevel()).minorBreakthrough();

    }

    public int getSpiritLevel() {
        return spiritLevel;
    }

    public static int calculateSpiritStrength(int spiritEnergy) {
        if (spiritEnergy <= 1)
            return 0;

        return (int)Math.log10(spiritEnergy - 1);
    }

    // should only be called outside of constructor if levels.clear() was called before
    protected void createMinorLevels(int sLevel) {
        for (int i = 0; i <= 9; i++) {

            // create minorLevel instance that is chained only if it's not the first element and not spirit level 0
            minorLevelType minorLevel = factory.createInstance(sLevel, !(i == 0 || sLevel == 0));

            levels.add(minorLevel);

            if (i == 0 && sLevel != 0) {
                levels.get(i).setToComplete();
            }
        }
    }
}
