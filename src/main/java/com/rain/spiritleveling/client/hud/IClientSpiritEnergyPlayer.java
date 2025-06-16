package com.rain.spiritleveling.client.hud;

public interface IClientSpiritEnergyPlayer {
    // PlayerEntity mixin functions to sync data
    void spirit_leveling$setDataMaxEnergy(int amount);
    void spirit_leveling$setDataCurrentEnergy(int amount);
    void spirit_leveling$setDataSpiritLevel(int amount);
    void spirit_leveling$setDataMinorBottleneck(boolean bool);

    int spirit_leveling$getDataMaxEnergy();
    int spirit_leveling$getDataCurrentEnergy();
    int spirit_leveling$getDataSpiritLevel();
    boolean spirit_leveling$getDataMinorBottleneck();
}
