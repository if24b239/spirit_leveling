package com.rain.spiritleveling.api;

import net.minecraft.nbt.NbtCompound;

public interface ISpiritEnergyPlayer {

    // ServerPlayerEntity mixin functions
    void spirit_leveling$addMaxSpiritEnergy(int amount);
    void spirit_leveling$addCurrentSpiritEnergy(int amount);
    void spirit_leveling$removeCurrentSpiritEnergy(int amount);
    void spirit_leveling$minorBreakthrough();
    void spirit_leveling$majorBreakthrough();
    void spirit_leveling$initSpiritEnergy(NbtCompound nbt);

}
