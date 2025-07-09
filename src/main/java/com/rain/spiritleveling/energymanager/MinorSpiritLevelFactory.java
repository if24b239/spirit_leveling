package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.Stages;

public interface MinorSpiritLevelFactory<T extends MinorSpiritLevel> {

    T createInstance(Stages sLevel, boolean chains);
}
