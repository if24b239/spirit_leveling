package com.rain.spiritleveling.events;

import com.faux.customentitydata.api.IPersistentDataHolder;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.client.IClientSpiritEnergyPlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModEvents {

    public static void initialize() {

        // load current and max spirit energy after respawn
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            NbtCompound nbt = ((IPersistentDataHolder)newPlayer).faux$getPersistentData();

            ((ISpiritEnergyPlayer)newPlayer).spirit_leveling$initSpiritEnergy(nbt);
        });

        // player joins event
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            NbtCompound nbt = ((IPersistentDataHolder)player).faux$getPersistentData();

            // initialize the spirit energy system
            ((ISpiritEnergyPlayer)player).spirit_leveling$initSpiritEnergy(nbt);

            // set client data on join
            IClientSpiritEnergyPlayer clientPlayer = (IClientSpiritEnergyPlayer)player;

            clientPlayer.spirit_leveling$setDataMaxEnergy(nbt.getInt("maxEnergy"));
            clientPlayer.spirit_leveling$setDataCurrentEnergy(nbt.getInt("currentEnergy"));
            clientPlayer.spirit_leveling$setDataSpiritLevel(nbt.getInt("spiritLevel"));
            clientPlayer.spirit_leveling$setDataMinorBottleneck(nbt.getBoolean("minorBottleneck"));
        });
    }
}
