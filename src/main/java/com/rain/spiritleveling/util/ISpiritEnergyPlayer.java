package com.rain.spiritleveling.util;

import net.minecraft.nbt.NbtCompound;

public interface ISpiritEnergyPlayer {

    void spirit_leveling$setMaxData(int amount);
    void spirit_leveling$setCurrentData(int amount);
    void spirit_leveling$setLevelData(int amount);
    void spirit_leveling$setPowerData(int amount);
    void spirit_leveling$setDrawLastChain(boolean bool);


    int spirit_leveling$getMaxData();
    int spirit_leveling$getCurrentData();
    int spirit_leveling$getLevelData();
    int spirit_leveling$getPowerData();
    boolean spirit_leveling$getDrawLastChain();


    // new!!!!!!!!!
    void spirit_leveling$addMaxSpiritEnergy(int amount);
    void spirit_leveling$addCurrentSpiritEnergy(int amount);
    void spirit_leveling$removeCurrentSpiritEnergy(int amount);
    void spirit_leveling$minorBreakthrough();
    void spirit_leveling$majorBreakthrough();
    void spirit_leveling$initSpiritEnergy(NbtCompound nbt);

}
