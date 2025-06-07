package com.rain.spiritleveling.util;

public interface ISpiritEnergyPlayer {

    void spirit_leveling$setMaxData(int amount);
    void spirit_leveling$setCurrentData(int amount);
    void spirit_leveling$setLevelData(int amount);

    int spirit_leveling$getMaxData();
    int spirit_leveling$getCurrentData();
    int spirit_leveling$getLevelData();

}
