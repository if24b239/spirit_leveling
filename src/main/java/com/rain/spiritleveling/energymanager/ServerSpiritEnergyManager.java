package com.rain.spiritleveling.energymanager;

import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.client.hud.IClientSpiritEnergyPlayer;
import com.rain.spiritleveling.util.SpiritAttributes;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerSpiritEnergyManager extends ServerMajorSpiritLevel {

    private int currentEnergy;
    private int maxEnergy;
    private Stages spiritPower;

    public ServerSpiritEnergyManager(ServerPlayerEntity player, int current_energy, int max_energy, Stages spirit_level, boolean minorBottleneck) {
        super(player, spirit_level, max_energy, minorBottleneck);
        spiritPower = calculateSpiritStrength(current_energy);
        currentEnergy = current_energy;
        maxEnergy = max_energy;

        updateModifiers();
    }

    // called to increase the maximum spirit energy while taking care of all bottlenecks
    public void addMaxEnergy(int amount) {
        amount = addProgress(amount);
        maxEnergy += amount;
    }

    public void addCurrentEnergy(int amount) {
        currentEnergy = Math.min(currentEnergy + amount, maxEnergy);
        updateSpiritPower();
        updateModifiers();
    }

    // returns false when amount is bigger than currentEnergy and doesn't change anything
    public boolean removeCurrentEnergy(int amount) {
        if (amount > currentEnergy) return false;

        currentEnergy -= amount;
        updateSpiritPower();
        updateModifiers();

        return true;
    }

    public void updateNbT() {
        NbtCompound nbt = ((ISpiritEnergyPlayer) player).spirit_leveling$getPersistentData();

        int minorLevel = getMinorLevel();

        nbt.putInt("maxEnergy", maxEnergy);
        nbt.putInt("currentEnergy", currentEnergy);
        nbt.putInt("spiritLevel", getSpiritLevel().getValue());

        if (minorLevel < 10)
            nbt.putBoolean("minorBottleneck", levels.get(minorLevel).getIsChained());


        ((ISpiritEnergyPlayer) player).spirit_leveling$savePersistentData(nbt);
    }

    public void updateClientData() {
        int minorLevel = getMinorLevel();

        ((IClientSpiritEnergyPlayer) player).spirit_leveling$setDataMaxEnergy(maxEnergy);
        ((IClientSpiritEnergyPlayer) player).spirit_leveling$setDataCurrentEnergy(currentEnergy);
        ((IClientSpiritEnergyPlayer) player).spirit_leveling$setDataSpiritLevel(getSpiritLevel());

        if (minorLevel < 10)
            ((IClientSpiritEnergyPlayer) player).spirit_leveling$setDataMinorBottleneck(levels.get(minorLevel).getIsChained());
    }

    @Override
    public boolean majorBreakthrough() {
         if (!super.majorBreakthrough()) return false;

        // add one max energy to sync up spiritLevel calculations with MajorSpiritLevel state
        addMaxEnergy(1);

        // update Spirit power attribute modifiers
        updateModifiers();
        return true;
    }

    private void updateModifiers() {

        EntityAttributeInstance instance = player.getAttributes().getCustomInstance(SpiritAttributes.SPIRIT_POWER);

        if (instance == null)
            throw new IllegalStateException(SpiritAttributes.SPIRIT_POWER.getTranslationKey() + " attribute not on entity");

        instance.clearModifiers();

        instance.addTemporaryModifier(getSpiritLevel().getLevelModifier());
        instance.addTemporaryModifier(getSpiritPower().getPowerModifier());
    }

    private void updateSpiritPower() {
        spiritPower = calculateSpiritStrength(currentEnergy);
    }

    public Stages getSpiritPower() {
        return spiritPower;
    }

    public boolean isAtMax() {
        return this.currentEnergy >= this.maxEnergy;
    }
}
