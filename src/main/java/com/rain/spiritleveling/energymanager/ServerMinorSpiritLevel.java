package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerMinorSpiritLevel extends MinorSpiritLevel{

    private ServerPlayerEntity player;
    private int index = 0;

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

        SpiritLeveling.LOGGER.info("{}",index);

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIntArray(new int[]{index, oldState.getValue(), newState.getValue()});
        ServerPlayNetworking.send(player, ModMessages.HUD_ANIMATION, buf);
    }

    // TODO will call the packet to play the chains removal animation
    private void updateChains() {
        if (player == null) return;
        // packet call here!
    }

    public void setPlayer(ServerPlayerEntity player) {
        this.player = player;
    }

    public void setIndex(int id) {
        this.index = id;
    }
}
