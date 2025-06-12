package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;

public class MinorSpiritLevel {

    private final int levelSize;
    private int progress = 0;
    private boolean isComplete = false;
    private boolean isChained;
    private COVER_STATE state;

    private MinorSpiritLevel(int spiritLevel, boolean chains) {
        levelSize = getLevelSize(spiritLevel);
        state = getCoverState();
        isChained = chains;
    }

    public static MinorSpiritLevel createMinorSpiritLevel(int spiritLevel, boolean chains) {
        return new MinorSpiritLevel(spiritLevel, chains);
    }

    // calculates how much total spirit power is needed to complete the minor level
    public static int getLevelSize(int spiritLevel) {
        return (int)Math.pow(10, spiritLevel);
    }

    public COVER_STATE getCoverState() {
        float ratio = (float) progress / levelSize;

        COVER_STATE newState = COVER_STATE.NONE;

        if (ratio < 0.33F) newState = COVER_STATE.FULL;

        if (ratio < 0.66F) newState = COVER_STATE.STRONG;

        if (ratio < 1F) newState = COVER_STATE.WEAK;

        if (state != newState)
            updateState(state, newState);

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

    // unlock minor level to progress it
    public void minorBreakthrough() {
        if (isChained) {
            isChained = false;
            updateChains();
        }
    }

    public boolean getIsComplete() { return isComplete; }
    public boolean getIsChained() { return isChained; }

    // TODO will call the packet to draw the animation
    private void updateState(COVER_STATE oldState, COVER_STATE newState) {
        // packet call here!
    }

    // TODO will call the packet to play the chains removal animation
    private void updateChains() {
        // packet call here!
    }
}
