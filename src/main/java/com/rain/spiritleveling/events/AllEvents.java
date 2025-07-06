package com.rain.spiritleveling.events;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.ISpiritEnergyPlayer;
import com.rain.spiritleveling.api.Stages;
import com.rain.spiritleveling.client.hud.IClientSpiritEnergyPlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class AllEvents {

    public static void initialize() {

        // load current and max spirit energy after respawn
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            // initialize the spirit energy system
            initEnergy(newPlayer);

            // make sure the attributes increase by breakthroughs stay after respawn
            respawnAttributeModifiers(oldPlayer, newPlayer);

            // player respawns with full health
            newPlayer.setHealth(newPlayer.getMaxHealth());

        });

        // player joins event
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            initEnergy(player);
        });
    }

    private static void respawnAttributeModifiers(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        NbtList attributeModifiers = ((ISpiritEnergyPlayer) oldPlayer).spirit_leveling$getPersistentData().getList("attributeModifiers", NbtElement.COMPOUND_TYPE);

        SpiritLeveling.LOGGER.info("{}", attributeModifiers);

        Map<EntityAttribute, EntityAttributeModifier> respawnModifiers = new HashMap<>();

        // get all modifiers with their attribute
        for (int i = 0; i < attributeModifiers.size(); i++) {
            UUID id = attributeModifiers.getCompound(i).getUuid("uuid");
            EntityAttribute attribute = Registries.ATTRIBUTE.get(new Identifier(attributeModifiers.getCompound(i).getString("attribute")));


            EntityAttributeInstance instance = oldPlayer.getAttributes().getCustomInstance(attribute);
            if (instance == null)
                throw new IllegalStateException("Attribute " + attribute + " not initialized on player");

            respawnModifiers.put(attribute, instance.getModifier(id));
        }

        // add all modifiers from respawnModifiers to the new player
        respawnModifiers.forEach((key, value) ->
             Objects.requireNonNull(newPlayer.getAttributes().getCustomInstance(key)).addPersistentModifier(value));
    }

    private static void initEnergy(ServerPlayerEntity player) {
        NbtCompound nbt = ((ISpiritEnergyPlayer)player).spirit_leveling$getPersistentData();

        // initialize the spirit energy system
        ((ISpiritEnergyPlayer)player).spirit_leveling$initSpiritEnergy(nbt);

        // set client data on join
        IClientSpiritEnergyPlayer clientNewPlayer = (IClientSpiritEnergyPlayer)player;
        clientNewPlayer.spirit_leveling$setDataMaxEnergy(nbt.getInt("maxEnergy"));
        clientNewPlayer.spirit_leveling$setDataCurrentEnergy(nbt.getInt("currentEnergy"));
        clientNewPlayer.spirit_leveling$setDataSpiritLevel(Stages.stateOf(nbt.getInt("spiritLevel")));
        clientNewPlayer.spirit_leveling$setDataMinorBottleneck(nbt.getBoolean("minorBottleneck"));
    }
}
