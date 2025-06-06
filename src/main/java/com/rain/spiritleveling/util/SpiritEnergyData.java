package com.rain.spiritleveling.util;

import com.faux.customentitydata.api.IPersistentDataHolder;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class SpiritEnergyData {

    // increase Max SE
    public static int addMaxSpiritEnergy(@NotNull IPersistentDataHolder player, int amount) {
        NbtCompound nbt = player.faux$getPersistentData();
        int currentMax = nbt.getInt("maxSpiritEnergy");

        currentMax += amount;

        nbt.putInt("maxSpiritEnergy", currentMax);

        // update the data on the ServerPlayerEntity
        player.faux$setPersistentData(nbt);
        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setMaxData(currentMax);

        return currentMax;
    }

    // regenerate SE
    public static int addCurrentSpiritEnergy(@NotNull IPersistentDataHolder player, int amount) {
        NbtCompound nbt = player.faux$getPersistentData();
        int currentEnergy = nbt.getInt("currentSpiritEnergy");
        int maxEnergy = nbt.getInt("maxSpiritEnergy");

        currentEnergy = Math.min(currentEnergy + amount, maxEnergy);

        nbt.putInt("currentSpiritEnergy", currentEnergy);

        // update the data on the ServerPlayerEntity
        player.faux$setPersistentData(nbt);
        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setCurrentData(currentEnergy);

        return currentEnergy;
    }

    // RETURNS -1 if amount is bigger than currentEnergy
    // TODO add attribute to reduce Energy consumption
    // consume SE
    public static int removeCurrentSpiritEnergy(@NotNull IPersistentDataHolder player, int amount) {
        NbtCompound nbt = player.faux$getPersistentData();
        int currentEnergy = nbt.getInt("currentSpiritEnergy");

        if ( amount > currentEnergy ) return -1;

        currentEnergy -= amount;
        nbt.putInt("currentSpiritEnergy", currentEnergy);

        // update the data on the ServerPlayerEntity
        player.faux$setPersistentData(nbt);
        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setCurrentData(currentEnergy);

        return currentEnergy;
    }


}
