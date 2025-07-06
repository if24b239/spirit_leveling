package com.rain.spiritleveling.util;

import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.energymanager.MinorSpiritLevel;

public class ClientMinorSpiritLevelFactory implements MinorSpiritLevelFactory<MinorSpiritLevel> {
    @Override
    public MinorSpiritLevel createInstance(Stages sLevel, boolean chains) {
        return new MinorSpiritLevel(sLevel, chains);
    }
}
