package com.rain.spiritleveling.entities;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.entities.custom.MeditationMatSitEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AllEntities {

    public static final EntityType<MeditationMatSitEntity> MAT_SIT_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            SpiritLeveling.loc("mat_sit_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MeditationMatSitEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 1.25f))
                    .trackRangeBlocks(10)
                    .build());

    private static <T extends Entity> EntityType<T> registerBasic(String path, EntityType.EntityFactory<T> factory, SpawnGroup group) {
        return Registry.register(Registries.ENTITY_TYPE, SpiritLeveling.loc(path),
                EntityType.Builder.create(factory, group).build(path));
    }


    public static void initialize() {
        SpiritLeveling.LOGGER.info("Registering all Entities");
    }
}
