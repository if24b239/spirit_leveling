package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.util.SpiritTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(AllBlocks.MEDITATION_MAT)
                .add(AllBlocks.JADE_STONE_BLOCK)
                .add(AllBlocks.JADE_DEEPSLATE_BLOCK)
                .add(AllBlocks.INFERIOR_JADE_ENERGY)
                .add(AllBlocks.BASIC_JADE_ENERGY)
                .add(AllBlocks.LOWER_JADE_ENERGY)
                .add(AllBlocks.INTERMEDIATE_JADE_ENERGY)
                .add(AllBlocks.HIGHER_JADE_ENERGY)
                .add(AllBlocks.ADVANCED_JADE_ENERGY)
                .add(AllBlocks.SUPERIOR_JADE_ENERGY);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(AllBlocks.JADE_STONE_BLOCK)
                .add(AllBlocks.JADE_DEEPSLATE_BLOCK)
                .add(AllBlocks.MEDITATION_MAT)
                .add(AllBlocks.INFERIOR_JADE_ENERGY)
                .add(AllBlocks.BASIC_JADE_ENERGY);

        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(AllBlocks.LOWER_JADE_ENERGY)
                .add(AllBlocks.INTERMEDIATE_JADE_ENERGY)
                .add(AllBlocks.HIGHER_JADE_ENERGY);

        getOrCreateTagBuilder(SpiritTags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(AllBlocks.ADVANCED_JADE_ENERGY)
                .add(AllBlocks.SUPERIOR_JADE_ENERGY);

    }
}
