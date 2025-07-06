package com.rain.spiritleveling.util;

import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.energymanager.ServerMinorSpiritLevel;

public class ServerMinorSpiritLevelFactory implements MinorSpiritLevelFactory<ServerMinorSpiritLevel> {
    @Override
    public ServerMinorSpiritLevel createInstance(Stages sLevel, boolean chains) {
        return new ServerMinorSpiritLevel(sLevel, chains);
    }
}
