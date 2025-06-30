package com.rain.spiritleveling.blocks;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import com.rain.spiritleveling.blocks.entity.SpiritEnergyStorageBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AllBlockEntities {

    public static final BlockEntityType<MeditationMatEntity> MEDITATION_MAT_ENTITY = registerBlockEntity("meditation_mat", BlockEntityType.Builder.create(MeditationMatEntity::new, AllBlocks.MEDITATION_MAT).build(null));
    public static final BlockEntityType<SpiritEnergyStorageBlockEntity> SPIRIT_STORAGE_ENTITY = registerBlockEntity("spirit_storage_entity", BlockEntityType.Builder.create(SpiritEnergyStorageBlockEntity::new,
            AllBlocks.INFERIOR_JADE_ENERGY,
            AllBlocks.BASIC_JADE_ENERGY,
            AllBlocks.LOWER_JADE_ENERGY,
            AllBlocks.INTERMEDIATE_JADE_ENERGY,
            AllBlocks.HIGHER_JADE_ENERGY,
            AllBlocks.ADVANCED_JADE_ENERGY,
            AllBlocks.SUPERIOR_JADE_ENERGY).build(null));

    private static <T extends BlockEntityType<?>> T registerBlockEntity(String path, T blockEntity) {

        return Registry.register(Registries.BLOCK_ENTITY_TYPE, SpiritLeveling.loc(path), blockEntity);
    }

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Initializing Block Entities");
    }
}
