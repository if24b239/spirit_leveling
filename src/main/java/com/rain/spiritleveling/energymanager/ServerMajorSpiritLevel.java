package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.networking.AllMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerMajorSpiritLevel extends MajorSpiritLevel<ServerMinorSpiritLevel> {

    protected final ServerPlayerEntity player;

    public ServerMajorSpiritLevel(ServerPlayerEntity player, Stages s_level, int max_energy, boolean minorBottleneck) {
        super(s_level, max_energy, minorBottleneck, new ServerMinorSpiritLevelFactory());
        this.player = player;

        // update minor levels with player data
        updateServerLevels();
    }

    @Override
    protected void createMinorLevels(Stages sLevel) {
        super.createMinorLevels(sLevel);

        // set index in reverse for easier location calculation in S2CPacket
        updateServerLevels();
    }

    @Override
    public boolean majorBreakthrough() {
        boolean success = super.majorBreakthrough();

        if (success) {
            clearAnimators();
        }

        return success;
    }

    private void updateServerLevels() {
        int index = 9;
        for (ServerMinorSpiritLevel l : levels) {
            l.setPlayer(player);
            l.setIndex(index--);
        }
    }

    private void clearAnimators() {
        if (player == null) return;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(true);
        ServerPlayNetworking.send(player, AllMessages.HUD_ANIMATION, buf);
    }
}
