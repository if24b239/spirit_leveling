package com.rain.spiritleveling.util;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.client.IClientSpiritEnergyPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ServerSpiritEnergyManager extends MajorSpiritLevel {

    private int currentEnergy;
    private int maxEnergy;
    private int spiritPower;

    public ServerSpiritEnergyManager(int current_energy, int max_energy, int spirit_level, boolean minorBottleneck) {
        super(spirit_level, max_energy, minorBottleneck);
        spiritPower = calculateSpiritStrength(current_energy);
        currentEnergy = current_energy;
        maxEnergy = max_energy;
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

    public void updateNbT(IPersistentDataHolder player) {
        NbtCompound nbt = new NbtCompound();

        int minorLevel = getMinorLevel();

        nbt.putInt("maxEnergy", maxEnergy);
        nbt.putInt("currentEnergy", currentEnergy);
        nbt.putInt("spiritLevel", getSpiritLevel());

        if (minorLevel < 10)
            nbt.putBoolean("minorBottleneck", levels.get(minorLevel).getIsChained());


        player.faux$setPersistentData(nbt);
    }

    public void updateClientData(IClientSpiritEnergyPlayer player) {
        int minorLevel = getMinorLevel();

        player.spirit_leveling$setDataMaxEnergy(maxEnergy);
        player.spirit_leveling$setDataCurrentEnergy(currentEnergy);
        player.spirit_leveling$setDataSpiritLevel(getSpiritLevel());

        if (minorLevel < 10)
            player.spirit_leveling$setDataMinorBottleneck(levels.get(minorLevel).getIsChained());
    }

    @Override
    public boolean majorBreakthrough() {
         if (!super.majorBreakthrough()) return false;

        // add one max energy to sync up spiritLevel calculations with MajorSpiritLevel state
        addMaxEnergy(1);
        return true;
    }

    private void updateSpiritPower() {
        spiritPower = calculateSpiritStrength(currentEnergy);
    }

}
