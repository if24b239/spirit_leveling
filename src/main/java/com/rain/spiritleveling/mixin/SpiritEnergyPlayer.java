package com.rain.spiritleveling.mixin;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.mojang.authlib.GameProfile;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.energymanager.ServerSpiritEnergyManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

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
    public boolean spirit_leveling$minorBreakthrough(int level) {
        if (!spiritLevelingSystem.minorBreakthrough(level)) {
            // TODO: Add punishment for failed breakthrough attempt
            return false;
        }

        spiritLevelingSystem.updateClientData();
        spiritLevelingSystem.updateNbT();
        return true;
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
    public void spirit_leveling$savePersistentData(NbtCompound nbt) {
        NbtCompound spiritCompound = ((IPersistentDataHolder) this).faux$getPersistentData();

        spiritCompound.put("spirit_leveling", nbt);

        ((IPersistentDataHolder) this).faux$setPersistentData(spiritCompound);
    }

    @Override
    public NbtCompound spirit_leveling$getPersistentData() {
        NbtCompound nbt = ((IPersistentDataHolder) this).faux$getPersistentData();

        return nbt.getCompound("spirit_leveling");
    }

    @Override
    public void spirit_leveling$addModifierUUID(Identifier identifier, UUID uuid) {
        NbtCompound nbt = this.spirit_leveling$getPersistentData();

        NbtList list = nbt.getList("attributeModifiers", NbtElement.COMPOUND_TYPE);

        // don't change nbt if UUID is already saved in it
        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getUuid("uuid") == uuid && Objects.equals(list.getCompound(i).getString("attribute"), identifier.toString()))
                return;
        }

        // add the new uuid and identifier into the list
        NbtCompound newCompound = new NbtCompound();
        newCompound.putUuid("uuid", uuid);
        newCompound.putString("attribute", identifier.toString());
        list.add(newCompound);

        nbt.put("attributeModifiers", list);

        this.spirit_leveling$savePersistentData(nbt);
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
