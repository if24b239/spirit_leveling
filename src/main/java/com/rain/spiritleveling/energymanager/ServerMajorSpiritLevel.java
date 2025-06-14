package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.util.MinorSpiritLevelFactory;
import com.rain.spiritleveling.util.ServerMinorSpiritLevelFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerMajorSpiritLevel extends MajorSpiritLevel<ServerMinorSpiritLevel> {

    protected final ServerPlayerEntity player;

    public ServerMajorSpiritLevel(ServerPlayerEntity player, int s_level, int max_energy, boolean minorBottleneck) {
        super(s_level, max_energy, minorBottleneck, new ServerMinorSpiritLevelFactory());
        this.player = player;

        // update minor levels with player data
        updateServerLevels();
    }

    @Override
    protected void createMinorLevels(int sLevel) {
        super.createMinorLevels(sLevel);

        // set index in reverse for easier location calculation in S2CPacket
        updateServerLevels();
    }

    private void updateServerLevels() {
        int index = 9;
        for (ServerMinorSpiritLevel l : levels) {
            l.setPlayer(player);
            l.setIndex(index--);
        }
    }
}
