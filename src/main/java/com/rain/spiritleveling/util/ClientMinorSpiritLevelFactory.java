package com.rain.spiritleveling.util;

import com.rain.spiritleveling.energymanager.MinorSpiritLevel;

public class ClientMinorSpiritLevelFactory implements MinorSpiritLevelFactory<MinorSpiritLevel> {
    @Override
    public MinorSpiritLevel createInstance(int sLevel, boolean chains) {
        return new MinorSpiritLevel(sLevel, chains);
    }
}
