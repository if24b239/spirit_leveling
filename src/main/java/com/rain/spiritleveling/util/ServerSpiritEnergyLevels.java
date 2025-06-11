package com.rain.spiritleveling.util;

public class ServerSpiritEnergyLevels  extends MajorSpiritLevel {

    private int currentEnergy;
    private int maxEnergy;
    public int spiritPower;

    public ServerSpiritEnergyLevels(int current_energy, int max_energy, int spirit_level) {
        super(spirit_level);
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
    }

    // returns false when amount is bigger than currentEnergy and doesn't change anything
    public boolean removeCurrentEnergy(int amount) {
        if (amount > currentEnergy) return false;

        currentEnergy -= amount;
        return true;
    }

    private void updateSpiritPower() {
        spiritPower = getSpiritLevel(currentEnergy);
    }

}
