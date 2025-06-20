package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.items.AllItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModBlockLootProvider extends FabricBlockLootTableProvider {
    public ModBlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(AllBlocks.MEDITATION_MAT);

        addDrop(AllBlocks.JADE_STONE_BLOCK, jadeChunkDrops(AllBlocks.JADE_STONE_BLOCK));
        addDrop(AllBlocks.JADE_DEEPSLATE_BLOCK, jadeChunkDrops(AllBlocks.JADE_DEEPSLATE_BLOCK));
    }

    private LootTable.Builder jadeChunkDrops(Block drop) {
        return dropsWithSilkTouch(
                drop,
                this.applyExplosionDecay(
                        drop,
                        ItemEntry.builder(AllItems.JADE_CHUNK)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                )
        );
    }
}
