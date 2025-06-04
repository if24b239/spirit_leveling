package com.rain.spiritleveling;


import io.github.fabricators_of_create.porting_lib.attributes.extensions.PlayerAttributesExtensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.ApiStatus;


public class SpiritAttributes {

    @ApiStatus.Internal
    public static void bootstrap() {
        // register the attributes
        register();
        // apply them to the entities
        apply();
    }

    private static void register() {

    }

    private static void apply() {
        
    }
}
