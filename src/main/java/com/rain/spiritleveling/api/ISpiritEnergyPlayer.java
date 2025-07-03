package com.rain.spiritleveling.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

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

    default void spirit_leveling$addModifierUUID(UUID id) {
        NbtCompound nbt = this.spirit_leveling$getPersistentData();

        NbtList list = nbt.getList("attributeModifiers", NbtElement.STRING_TYPE);

        if (list == null)
            list = new NbtList();

        // don't change nbt if UUID is already saved in it
        for (NbtElement e : list) {
            if (Objects.equals(e.asString(), id.toString()))
                return;
        }

        // add the uuid to the
        list.add(NbtString.of(id.toString()));

        nbt.put("attributeModifiers", list);

        this.spirit_leveling$savePersistentData(nbt);
    }

    int spirit_leveling$getSpiritPower();
    int spirit_leveling$getSpiritLevel();

    boolean spirit_leveling$isAtMax();
}
