package com.rain.spiritleveling.api;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class SpiritEnergyData {

    // increase Max SE
    public static void addMaxSpiritEnergy(@NotNull ServerPlayerEntity player, int amount) {
        NbtCompound nbt = ((IPersistentDataHolder)player).faux$getPersistentData();
        int currentMax = nbt.getInt("maxSpiritEnergy");
        int currentLevel = nbt.getInt("spiritLevel");
        int minorLevel = getMinorLevel(currentMax, currentLevel);
        boolean minorBottleneck = nbt.getBoolean("minorBottleneck");
        boolean majorBottleneck = nbt.getBoolean("majorBottleneck");

        if (minorBottleneck || majorBottleneck) return;

        int newMax = currentMax + amount;

        if (newMax >= (int)Math.pow(10, currentLevel + 1) && minorLevel >= 9) { // when it reaches 10, 100, 1000 usw
            majorBottleneck((IPersistentDataHolder) player);
            newMax = (int)Math.pow(10, currentLevel + 1);
            if (!majorBreakthrough((IPersistentDataHolder) player)) {
                SpiritLeveling.LOGGER.info("MAJOR BOTTLENECK RETURNED FALSE");
            }
        } else if (newMax >= (int)Math.pow(10, currentLevel) * (minorLevel + 1)) {
            minorBottleneck((IPersistentDataHolder) player);
            newMax = (int)Math.pow(10, currentLevel) * (minorLevel + 1);
            if (!minorBreakthrough((IPersistentDataHolder) player)) {
                SpiritLeveling.LOGGER.info("MINOR BOTTLENECK RETURNED FALSE");
            }
        }

        nbt.putInt("maxSpiritEnergy", newMax);

        // update the data on the ServerPlayerEntity
        ((IPersistentDataHolder)player).faux$setPersistentData(nbt);

        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setMaxData(newMax);
    }

    // regenerate SE
    public static void addCurrentSpiritEnergy(@NotNull ServerPlayerEntity player, int amount) {
        NbtCompound nbt = ((IPersistentDataHolder)player).faux$getPersistentData();
        int currentEnergy = nbt.getInt("currentSpiritEnergy");
        int maxEnergy = nbt.getInt("maxSpiritEnergy");

        currentEnergy = Math.min(currentEnergy + amount, maxEnergy);

        nbt.putInt("currentSpiritEnergy", currentEnergy);

        // update Spirit Power based on new currentEnergy
        updateSpiritPower((IPersistentDataHolder)player);

        // update the data on the ServerPlayerEntity
        ((IPersistentDataHolder)player).faux$setPersistentData(nbt);

        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setCurrentData(currentEnergy);
    }

    // RETURNS -1 if amount is bigger than currentEnergy
    // TODO add attribute to reduce Energy consumption
    // consume SE
    public static int removeCurrentSpiritEnergy(@NotNull ServerPlayerEntity player, int amount) {
        NbtCompound nbt = ((IPersistentDataHolder)player).faux$getPersistentData();
        int currentEnergy = nbt.getInt("currentSpiritEnergy");

        if ( amount > currentEnergy ) return -1;

        currentEnergy -= amount;
        nbt.putInt("currentSpiritEnergy", currentEnergy);

        // update Spirit Power based on new currentEnergy
        updateSpiritPower((IPersistentDataHolder)player);

        // update the data on the ServerPlayerEntity
        ((IPersistentDataHolder)player).faux$setPersistentData(nbt);

        // sync the data to client
        ((ISpiritEnergyPlayer) player).spirit_leveling$setCurrentData(currentEnergy);

        return currentEnergy;
    }

    public static boolean minorBreakthrough(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        boolean minorBottleneck = nbt.getBoolean("minorBottleneck");

        if (!minorBottleneck) return false;

        nbt.putBoolean("minorBottleneck", false);

        player.faux$setPersistentData(nbt);

        SpiritLeveling.LOGGER.info("MINOR BOTTLENECK BROKEN!");

        return true;
    }

    // returns false if the player wasn't in a major bottleneck otherwise increases spiritLevel and returns true
    public static boolean majorBreakthrough(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        boolean majorBottleneck = nbt.getBoolean("majorBottleneck");
        int currentSpiritLevel = nbt.getInt("spiritLevel");

        if (!majorBottleneck) return false;

        nbt.putInt("spiritLevel", ++currentSpiritLevel);
        nbt.putBoolean("majorBottleneck", false);

        // sync with client
        ((ISpiritEnergyPlayer)player).spirit_leveling$setLevelData(currentSpiritLevel);

        player.faux$setPersistentData(nbt);

        SpiritLeveling.LOGGER.info("MAJOR BOTTLENECK BROKEN!");

        return true;
    }

    private static void minorBottleneck(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        nbt.putBoolean("minorBottleneck", true);
        player.faux$setPersistentData(nbt);
    }

    private static void majorBottleneck(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        nbt.putBoolean("majorBottleneck", true);
        player.faux$setPersistentData(nbt);
    }

    // updates spirit power based on current spirit energy
    private static void updateSpiritPower(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        int currentSE = nbt.getInt("currentSpiritEnergy");

        // prevent problems with negative and 0 log10()
        if (currentSE < 2) currentSE = 2;

        int power = (int)Math.log10(currentSE - 1);

        nbt.putInt("spiritPower", power);

        // sync with client
        ((ISpiritEnergyPlayer)player).spirit_leveling$setPowerData(power);

        player.faux$setPersistentData(nbt);
    }

    public static void updateSpiritLevel(@NotNull IPersistentDataHolder player) {
        NbtCompound nbt = player.faux$getPersistentData();
        int maxSE = nbt.getInt("maxSpiritEnergy");

        // prevent problems with negative and 0 log10()
        if (maxSE < 2) maxSE = 2;

        int level = (int)Math.log10(maxSE - 1);

        nbt.putInt("spiritLevel", level);

        //sync with client
        ((ISpiritEnergyPlayer)player).spirit_leveling$setLevelData(level);

        player.faux$setPersistentData(nbt);
    }

    public static int getMinorLevel(int maxSpiritEnergy, int spiritLevel) {
        return (int)(maxSpiritEnergy / Math.pow(10, spiritLevel));
    }
}
