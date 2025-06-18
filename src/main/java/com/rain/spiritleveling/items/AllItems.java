package com.rain.spiritleveling.items;

import com.mojang.datafixers.TypeRewriteRule;
import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.items.custom.SpiritPill;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class AllItems {

    private AllItems() {}

    // all custom item instances
    public static final SpiritPill SPIRIT_PILL = register("spirit_pill", new SpiritPill(new Item.Settings(), 0));
    public static final SpiritPill S_SPIRIT_PILL = register("s_spirit_pill", new SpiritPill(new Item.Settings(), 4));

    // all basic item instances
    public static final Item JADE_CHUNK = register("jade_chunk", new Item(new Item.Settings()));

    // all block item instances
    public static final Item MEDITATION_MAT = AllBlocks.registerBlockItem("meditation_mat", AllBlocks.MEDITATION_MAT);
    public static final Item JADE_STONE_BLOCK = AllBlocks.registerBlockItem("jade_stone_block", AllBlocks.JADE_STONE_BLOCK);
    public static final Item JADE_DEEPSLATE_BLOCK = AllBlocks.registerBlockItem("jade_deepslate_block", AllBlocks.JADE_DEEPSLATE_BLOCK);

    // item creative tab
    public static final ItemGroup SPIRIT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPIRIT_PILL))
            .displayName(Text.translatable("itemGroup.spiritleveling.item_group"))
            .entries((context, entries) -> {
                //custom
                entries.add(SPIRIT_PILL);
                entries.add(S_SPIRIT_PILL);

                //item
                entries.add(JADE_CHUNK);

                //block
                entries.add(MEDITATION_MAT);
                entries.add(JADE_STONE_BLOCK);
                entries.add(JADE_DEEPSLATE_BLOCK);
            })
            .build();

    // helper to register items
    private static <T extends Item> T register(String path, T item) {

        return Registry.register(Registries.ITEM, SpiritLeveling.loc(path), item);
    }

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Registering mod Items");

        Registry.register(Registries.ITEM_GROUP, SpiritLeveling.loc("spirit_items_group"), SPIRIT_ITEM_GROUP);
    }
}
