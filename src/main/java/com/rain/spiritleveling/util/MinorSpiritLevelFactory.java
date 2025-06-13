package com.rain.spiritleveling.util;

import com.rain.spiritleveling.energymanager.MinorSpiritLevel;

public interface MinorSpiritLevelFactory<T extends MinorSpiritLevel> {

    T createInstance(int sLevel, boolean chains);
}
