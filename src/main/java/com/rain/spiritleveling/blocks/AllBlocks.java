package com.rain.spiritleveling.blocks;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.custom.MeditationMat;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AllBlocks {

    public static final MeditationMat MEDITATION_MAT = registerBlock("meditation_mat", new MeditationMat(AbstractBlock.Settings.create()));

    private static <T extends Block> T registerBlock(String path, T block) {
        return Registry.register(Registries.BLOCK, SpiritLeveling.loc(path), block);
    }

    public static <T extends Block> Item registerBlockItem(String path, T block) {

        return Registry.register(Registries.ITEM, SpiritLeveling.loc(path),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Registering mod Blocks and Block Items");

    }
}
