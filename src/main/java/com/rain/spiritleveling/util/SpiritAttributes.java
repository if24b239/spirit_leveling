package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.attributesmod.api.DynamicEntityAttribute;
import net.puffish.attributesmod.api.DynamicModification;

public class SpiritAttributes {

    /**
     * DON'T ADD MODIFIERS should only be changed by changes in Max and current spirit energy
     * Query by calling
     */
    public static final Identifier SPIRIT_POWER_ID = SpiritLeveling.loc("spirit_power");
    public static final EntityAttribute SPIRIT_POWER = DynamicEntityAttribute.create(SPIRIT_POWER_ID);

    public static final Identifier SPIRIT_REGENERATION_ID = SpiritLeveling.loc("spirit_regeneration");
    public static final EntityAttribute SPIRIT_REGENERATION = DynamicEntityAttribute.create(SPIRIT_REGENERATION_ID);

    public static void initialize() {

        Registry.register(Registries.ATTRIBUTE, SPIRIT_POWER_ID, SPIRIT_POWER);
        Registry.register(Registries.ATTRIBUTE, SPIRIT_REGENERATION_ID, SPIRIT_REGENERATION);
    }

    /**
     * @param player the ServerPlayerEntity being queried for its spirit power attribute
     * @return the spirit power attribute of player as a double
     */
    public static double getSpiritPower(ServerPlayerEntity player) {
        return DynamicModification.create().withPositive(SPIRIT_POWER, player).applyTo(0);
    }
}
