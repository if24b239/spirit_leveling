package com.rain.spiritleveling.util;

import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.energymanager.MinorSpiritLevel;

public interface MinorSpiritLevelFactory<T extends MinorSpiritLevel> {

    T createInstance(Stages sLevel, boolean chains);
}
