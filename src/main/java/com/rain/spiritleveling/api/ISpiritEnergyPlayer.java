package com.rain.spiritleveling.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.UUID;

public interface ISpiritEnergyPlayer {

    // ServerPlayerEntity mixin functions
    void spirit_leveling$addMaxSpiritEnergy(int amount);
    void spirit_leveling$addCurrentSpiritEnergy(int amount);
    boolean spirit_leveling$removeCurrentSpiritEnergy(int amount);

    boolean spirit_leveling$minorBreakthrough(int level);
    void spirit_leveling$majorBreakthrough();

    void spirit_leveling$initSpiritEnergy(NbtCompound nbt);

    void spirit_leveling$savePersistentData(NbtCompound nbt);
    NbtCompound spirit_leveling$getPersistentData();

    void spirit_leveling$addModifierUUID(Identifier identifier, UUID id);

    int spirit_leveling$getSpiritPower();
    int spirit_leveling$getSpiritLevel();

    boolean spirit_leveling$isAtMax();
}
