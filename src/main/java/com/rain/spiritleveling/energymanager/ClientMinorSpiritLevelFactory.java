package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.Stages;

public class ClientMinorSpiritLevelFactory implements MinorSpiritLevelFactory<MinorSpiritLevel> {
    @Override
    public MinorSpiritLevel createInstance(Stages sLevel, boolean chains) {
        return new MinorSpiritLevel(sLevel, chains);
    }
}
