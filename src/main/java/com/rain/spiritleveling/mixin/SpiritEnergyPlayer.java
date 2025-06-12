package com.rain.spiritleveling.mixin;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.mojang.authlib.GameProfile;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import com.rain.spiritleveling.util.ServerSpiritEnergyLevels;
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
    private ServerSpiritEnergyLevels spiritLevelingSystem;

    @Inject(method= "<init>", at = @At("RETURN"))
    private void onSpiritEnergyPlayerConstruct(MinecraftServer server, ServerWorld world, GameProfile profile, CallbackInfo ci) {
        NbtCompound nbt = ServerSpiritEnergyLevels.getNbt((IPersistentDataHolder)this);

        int currentEnergy = nbt.getInt("currentEnergy");
        int maxEnergy = nbt.getInt("maxEnergy");
        int spiritLevel = nbt.getInt("spiritLevel");
        boolean minorBottleneck = nbt.getBoolean("minorBottleneck");

        spiritLevelingSystem = new ServerSpiritEnergyLevels(currentEnergy, maxEnergy, spiritLevel, minorBottleneck);
    }

    @Override
    public void spirit_leveling$addMaxSpiritEnergy(int amount) {

        spiritLevelingSystem.addMaxEnergy(amount);

        spiritLevelingSystem.updateNbT((IPersistentDataHolder)this);
    }

    @Override
    public void spirit_leveling$addCurrentSpiritEnergy(int amount) {

        spiritLevelingSystem.addCurrentEnergy(amount);

        spiritLevelingSystem.updateNbT((IPersistentDataHolder)this);
    }

    @Override
    public void spirit_leveling$removeCurrentSpiritEnergy(int amount) {

        if (!spiritLevelingSystem.removeCurrentEnergy(amount)) {
            // do something

        }

        spiritLevelingSystem.updateNbT((IPersistentDataHolder)this);
    }
}
