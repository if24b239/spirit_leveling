package com.rain.spiritleveling.blocks;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AllBlockEntities {

    public static final BlockEntityType<MeditationMatEntity> MEDITATION_MAT_ENTITY = registerBlockEntity("meditation_mat", BlockEntityType.Builder.create(MeditationMatEntity::new, AllBlocks.MEDITATION_MAT).build(null));


    private static <T extends BlockEntityType<?>> T registerBlockEntity(String path, T blockEntity) {

        return Registry.register(Registries.BLOCK_ENTITY_TYPE, SpiritLeveling.loc(path), blockEntity);
    }

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Initializing Block Entities");
    }
}
