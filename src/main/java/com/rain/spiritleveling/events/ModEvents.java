package com.rain.spiritleveling.events;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.rain.spiritleveling.util.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.SpiritEnergyData;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModEvents {

    public static void initialize() {

        // load current and max spirit energy after respawn
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            int maxSpiritEnergy = ((ISpiritEnergyPlayer)oldPlayer).spirit_leveling$getMaxData();
            int currentSpiritEnergy = ((ISpiritEnergyPlayer)oldPlayer).spirit_leveling$getCurrentData();

            ((ISpiritEnergyPlayer)newPlayer).spirit_leveling$setMaxData(maxSpiritEnergy);
            ((ISpiritEnergyPlayer)newPlayer).spirit_leveling$setCurrentData(currentSpiritEnergy);
        });

        // player joins event
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            NbtCompound nbt = ((IPersistentDataHolder)player).faux$getPersistentData();

            // also updates client side data
            SpiritEnergyData.updateSpiritLevel((IPersistentDataHolder) player);
            SpiritEnergyData.updateSpiritPower((IPersistentDataHolder) player);

            int maxSpiritEnergy = nbt.getInt("maxSpiritEnergy");
            int currentSpiritEnergy= nbt.getInt("currentSpiritEnergy");
            boolean minorBottleneck = nbt.getBoolean("minorBottleneck");

            ((ISpiritEnergyPlayer)player).spirit_leveling$setMaxData(maxSpiritEnergy);
            ((ISpiritEnergyPlayer)player).spirit_leveling$setCurrentData(currentSpiritEnergy);
            ((ISpiritEnergyPlayer)player).spirit_leveling$setMinorBottleneck(minorBottleneck);
        });
    }
}
