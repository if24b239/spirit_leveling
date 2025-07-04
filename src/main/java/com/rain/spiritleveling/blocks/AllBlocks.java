package com.rain.spiritleveling.blocks;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.custom.MeditationMat;
import com.rain.spiritleveling.blocks.custom.SpiritEnergyBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class AllBlocks {

    // custom blocks
    public static final MeditationMat MEDITATION_MAT = registerBlock("meditation_mat", new MeditationMat(AbstractBlock.Settings.create()));
    public static final SpiritEnergyBlock INFERIOR_JADE_ENERGY = registerBlock("inferior_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 1));
    public static final SpiritEnergyBlock BASIC_JADE_ENERGY = registerBlock("basic_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 10));
    public static final SpiritEnergyBlock LOWER_JADE_ENERGY = registerBlock("lower_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 100));
    public static final SpiritEnergyBlock INTERMEDIATE_JADE_ENERGY = registerBlock("intermediate_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 1000));
    public static final SpiritEnergyBlock HIGHER_JADE_ENERGY = registerBlock("higher_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 10000));
    public static final SpiritEnergyBlock ADVANCED_JADE_ENERGY = registerBlock("advanced_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 100000));
    public static final SpiritEnergyBlock SUPERIOR_JADE_ENERGY = registerBlock("superior_jade_energy", new SpiritEnergyBlock(AbstractBlock.Settings.create(), 1000000));

    // basic blocks
    public static final Block JADE_STONE_BLOCK = registerBlock("jade_stone_block",
            new Block(FabricBlockSettings.create()
                    .requiresTool()
                    .strength(3.0f, 3.0f)
                    .sounds(BlockSoundGroup.STONE)
            )
    );
    public static final Block JADE_DEEPSLATE_BLOCK = registerBlock("jade_deepslate_block",
            new Block(FabricBlockSettings.create()
                    .requiresTool()
                    .strength(4.5f, 3.0f)
                    .sounds(BlockSoundGroup.DEEPSLATE)
            )
    );

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
