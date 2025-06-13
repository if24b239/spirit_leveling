package com.rain.spiritleveling.energymanager;

import net.minecraft.server.network.ServerPlayerEntity;

public class ServerMinorSpiritLevel extends MinorSpiritLevel{

    private final ServerPlayerEntity player;

    public ServerMinorSpiritLevel(int spiritLevel, boolean chains) {
        super(spiritLevel, chains);

        player = null;
    }

    @Override
    public COVER_STATE getCoverState() {
        COVER_STATE new_state = super.getCoverState();

        if (state != new_state)
            updateState(state, new_state);

        return new_state;
    }

    @Override
    public boolean minorBreakthrough() {
        boolean result = super.minorBreakthrough();

        if (result)
            updateChains();

        return result;
    }

    // TODO will call the packet to draw the animation
    private void updateState(COVER_STATE oldState, COVER_STATE newState) {
        if (player == null) return;
        // packet call here!

    }

    // TODO will call the packet to play the chains removal animation
    private void updateChains() {
        if (player == null) return;
        // packet call here!
    }
}
