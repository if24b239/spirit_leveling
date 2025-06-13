package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;

import java.util.ArrayList;

public class MajorSpiritLevel {

    private int spiritLevel;
    private boolean isComplete = false;

    ArrayList<MinorSpiritLevel> levels = new ArrayList<>();

    public MajorSpiritLevel(int s_level, int max_energy, boolean minorBottleneck) {
        spiritLevel = s_level;

        createMinorLevels(s_level);

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
        minorBreakthrough();

        return true;
    }

    // will remove chains off the next minor level
    public void minorBreakthrough() {
        if (isComplete) return;

        levels.get(getMinorLevel()).minorBreakthrough();
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
    private void createMinorLevels(int sLevel) {
        for (int i = 0; i <= 9; i++) {

            // create minorLevel instance that is chained only if it's not the first element and not spirit level 0
            MinorSpiritLevel minorLevel = MinorSpiritLevel.createMinorSpiritLevel(sLevel, !(i == 0 || sLevel == 0));

            levels.add(minorLevel);

            if (i == 0 && sLevel != 0) {
                levels.get(i).setToComplete();
            }
        }
    }
}
