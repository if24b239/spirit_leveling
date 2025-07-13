package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.Stages;

public class MinorSpiritLevel {

    private final int levelSize;
    private int progress = 0;
    private boolean isComplete = false;
    private boolean isChained;
    public COVER_STATE state;

    public MinorSpiritLevel(Stages spiritLevel, boolean chains) {
        levelSize = spiritLevel.getSpiritEnergy();
        state = getCoverState();
        isChained = chains;
    }

    public COVER_STATE getCoverState() {
        float ratio = (float) progress / levelSize;

        COVER_STATE newState;

        if (ratio >= 1F) newState = COVER_STATE.NONE;

        else if (ratio >= 0.66F) newState = COVER_STATE.WEAK;

        else if (ratio >= 0.33F) newState = COVER_STATE.STRONG;

        else newState = COVER_STATE.FULL;

        return newState;
    }

    // returns the actually added amount
    public int addProgress(int amount) {
        // no changes if the minorLevel is still chained
        if (isChained) return 0;

        // minor level is complete
        if (progress + amount >= levelSize) {

            int actual_amount = levelSize - progress;
            isComplete = true;
            progress = levelSize;
            state = getCoverState();

            return actual_amount;
        }

        progress += amount;
        state = getCoverState();
        return amount;
    }

    public void setToComplete() {
        isComplete = true;
    }

    // unlock minor level to progress it (return true if successful false when not)
    public boolean minorBreakthrough() {
        if (isChained) {
            isChained = false;
            return true;
        }

        return false;
    }

    public boolean getIsComplete() { return isComplete; }
    public boolean getIsChained() { return isChained; }

}
