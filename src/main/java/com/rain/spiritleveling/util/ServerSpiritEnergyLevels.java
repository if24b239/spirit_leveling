package com.rain.spiritleveling.util;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.nbt.NbtCompound;

public class ServerSpiritEnergyLevels  extends MajorSpiritLevel {

    private int currentEnergy;
    private int maxEnergy;
    private int spiritPower;

    public ServerSpiritEnergyLevels(int current_energy, int max_energy, int spirit_level, boolean minorBottleneck) {
        super(spirit_level, max_energy, minorBottleneck);
        spiritPower = getSpiritLevel(current_energy);
        currentEnergy = current_energy;
        maxEnergy = max_energy;
    }

    public static int getSpiritLevel(int spiritEnergy) {
        return (int)Math.log(spiritEnergy - 1);
    }

    // called to increase the maximum spirit energy while taking care of all bottlenecks
    public void addMaxEnergy(int amount) {
        amount = addProgress(amount);
        maxEnergy += amount;
    }

    public void addCurrentEnergy(int amount) {
        currentEnergy = Math.min(currentEnergy + amount, maxEnergy);
        updateSpiritPower();
    }

    // returns false when amount is bigger than currentEnergy and doesn't change anything
    public boolean removeCurrentEnergy(int amount) {
        if (amount > currentEnergy) return false;

        currentEnergy -= amount;
        updateSpiritPower();

        return true;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }
    public int getSpiritPower() {
        return spiritPower;
    }

    public void updateNbT(IPersistentDataHolder player) {
        NbtCompound nbt = new NbtCompound();

        nbt.putInt("maxEnergy", maxEnergy);
        nbt.putInt("currentEnergy", currentEnergy);
        nbt.putInt("spiritLevel", getSpiritLevel());
        nbt.putBoolean("minorBottleneck", levels.get(getMinorLevel()).getIsChained());

        player.faux$setPersistentData(nbt);
    }

    public static NbtCompound getNbt(IPersistentDataHolder player) {
        return player.faux$getPersistentData();
    }

    private void updateSpiritPower() {
        spiritPower = getSpiritLevel(currentEnergy);
    }

}
