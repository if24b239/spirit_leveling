package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.Stages;

public class ServerMinorSpiritLevelFactory implements MinorSpiritLevelFactory<ServerMinorSpiritLevel> {
    @Override
    public ServerMinorSpiritLevel createInstance(Stages sLevel, boolean chains) {
        return new ServerMinorSpiritLevel(sLevel, chains);
    }
}
