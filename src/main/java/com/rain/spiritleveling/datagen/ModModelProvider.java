package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.items.AllItems;
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

        //blockStateModelGenerator.registerAxisRotated(AllBlocks.MEDITATION_MAT, );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        itemModelGenerator.register(AllItems.SPIRIT_PILL, Models.GENERATED);
        itemModelGenerator.register(AllBlocks.MEDITATION_MAT.asItem(), new Model(Optional.of(SpiritLeveling.loc("block/meditation_mat")), Optional.empty()));
        itemModelGenerator.register(AllItems.JADE_CHUNK, Models.GENERATED);
    }
}
