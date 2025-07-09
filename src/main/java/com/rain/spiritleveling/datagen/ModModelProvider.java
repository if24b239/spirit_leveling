package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.items.AllItems;
import com.rain.spiritleveling.items.custom.CultivationManual;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.JADE_STONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.JADE_DEEPSLATE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.INFERIOR_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.BASIC_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.LOWER_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.INTERMEDIATE_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.HIGHER_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.ADVANCED_JADE_ENERGY);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.SUPERIOR_JADE_ENERGY);

        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(AllBlocks.MEDITATION_MAT);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        itemModelGenerator.register(AllItems.SPIRIT_PILL, Models.GENERATED);
        itemModelGenerator.register(AllItems.JADE_CHUNK, Models.GENERATED);
        itemModelGenerator.register(AllItems.IMPERIAL_JADE, Models.GENERATED);
        itemModelGenerator.register(AllItems.ABRASION_SAND, Models.GENERATED);
        itemModelGenerator.register(AllItems.BOW_AND_DRILL, Models.GENERATED);
        itemModelGenerator.register(AllItems.DRILL, Models.GENERATED);
        itemModelGenerator.register(AllBlocks.MEDITATION_MAT.asItem(), new Model(Optional.of(SpiritLeveling.loc("block/meditation_mat")), Optional.empty()));
        CultivationManual.generateModel(itemModelGenerator, AllItems.SPIRIT_CONDENSATION_BODY_MANUAL);
    }
}
