package com.rain.spiritleveling.mixin;

import com.mojang.authlib.GameProfile;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.energymanager.ServerSpiritEnergyManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class SpiritEnergyPlayer implements ISpiritEnergyPlayer {

    @Unique
    private ServerSpiritEnergyManager spiritLevelingSystem;

    @Inject(method= "<init>", at = @At("RETURN"))
    private void onSpiritEnergyPlayerConstruct(MinecraftServer server, ServerWorld world, GameProfile profile, CallbackInfo ci) {
        spiritLevelingSystem = new ServerSpiritEnergyManager((ServerPlayerEntity) (Object) this,0,0,0,false);
    }

    @Override
    public void spirit_leveling$addMaxSpiritEnergy(int amount) {

        spiritLevelingSystem.addMaxEnergy(amount);

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();
    }

    @Override
    public void spirit_leveling$addCurrentSpiritEnergy(int amount) {

        spiritLevelingSystem.addCurrentEnergy(amount);

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();
    }

    @Override
    public boolean spirit_leveling$removeCurrentSpiritEnergy(int amount) {

        if (!spiritLevelingSystem.removeCurrentEnergy(amount)) {
            // do something
            return false;
        }

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();

        return true;
    }

    @Override
    public void spirit_leveling$minorBreakthrough() {
        spiritLevelingSystem.minorBreakthrough();

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();
    }

    @Override
    public void spirit_leveling$majorBreakthrough() {
        spiritLevelingSystem.majorBreakthrough();

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();
    }

    @Override
    public void spirit_leveling$initSpiritEnergy(NbtCompound nbt) {

        int currentEnergy = nbt.getInt("currentEnergy");
        int maxEnergy = nbt.getInt("maxEnergy");
        int spiritLevel = nbt.getInt("spiritLevel");
        boolean minorBottleneck = nbt.getBoolean("minorBottleneck");


        spiritLevelingSystem = new ServerSpiritEnergyManager((ServerPlayerEntity) (Object) this,currentEnergy, maxEnergy, spiritLevel, minorBottleneck);
    }

    @Override
    public int spirit_leveling$getSpiritPower() {
        return spiritLevelingSystem.getSpiritPower();
    }

    @Override
    public int spirit_leveling$getSpiritLevel() {
        return spiritLevelingSystem.getSpiritLevel();
    }

    @Override
    public boolean spirit_leveling$isAtMax() {
        return spiritLevelingSystem.isAtMax();
    }


}
