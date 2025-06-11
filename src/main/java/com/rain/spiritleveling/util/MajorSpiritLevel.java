package com.rain.spiritleveling.util;

import java.util.ArrayList;

public class MajorSpiritLevel {

    private int spiritLevel;
    private boolean isComplete = false;

    ArrayList<MinorSpiritLevel> levels = new ArrayList<>();

    public MajorSpiritLevel(int sLevel) {
        spiritLevel = sLevel;

        createMinorLevels(sLevel);
    }

    // returns the number of completed minor levels
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
        assert minorLevel < 10;
        int newAmount = levels.get(minorLevel).addProgress(amount);

        // set the major level to complete if after adding the progress all 10 minor levels are complete
        if (getMinorLevel() >= 10) isComplete = true;

        return newAmount;
    }

    // increases spiritLevel and remakes the minor levels as long as the current major level is complete
    public void majorBreakthrough() {
        if (!isComplete) return;

        spiritLevel++;

        levels.clear();
        createMinorLevels(spiritLevel);
    }

    // will remove chains off the next minor level
    public void minorBreakthrough() {
        if (isComplete) return;

        levels.get(getMinorLevel()).minorBreakthrough();
    }

    // should only be called outside of constructor if levels.clear() was called before
    private void createMinorLevels(int sLevel) {
        for (int i = 0; i <= 9; i++) {

            // create minorLevel instance that is chained only if it's not the first element and not spirit level 0
            MinorSpiritLevel minorLevel = MinorSpiritLevel.createMinorSpiritLevel(sLevel, !(i == 0 || sLevel == 0));

            // complete the first level on any spirit Level above 0 since they are already unlocked
            if (i == 0 && sLevel != 0) {
                minorLevel.addProgress(MinorSpiritLevel.getLevelSize(sLevel));
            }

            levels.add(minorLevel);
        }
    }
}
