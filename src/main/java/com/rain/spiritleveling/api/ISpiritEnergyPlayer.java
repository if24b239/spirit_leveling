package com.rain.spiritleveling.api;

import net.minecraft.nbt.NbtCompound;

public interface ISpiritEnergyPlayer {

    // ServerPlayerEntity mixin functions
    void spirit_leveling$addMaxSpiritEnergy(int amount);
    void spirit_leveling$addCurrentSpiritEnergy(int amount);
    boolean spirit_leveling$removeCurrentSpiritEnergy(int amount);
    void spirit_leveling$minorBreakthrough();
    void spirit_leveling$majorBreakthrough();
    void spirit_leveling$initSpiritEnergy(NbtCompound nbt);

    int spirit_leveling$getSpiritPower();
    int spirit_leveling$getSpiritLevel();

    boolean spirit_leveling$isAtMax();
}
