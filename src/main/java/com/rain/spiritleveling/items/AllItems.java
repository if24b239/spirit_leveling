package com.rain.spiritleveling.items;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Elements;
import com.rain.spiritleveling.blocks.AllBlocks;
import com.rain.spiritleveling.items.custom.BowAndDrill;
import com.rain.spiritleveling.items.custom.CultivationManual;
import com.rain.spiritleveling.items.custom.SpiritPill;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
    public static final BowAndDrill BOW_AND_DRILL = register("bow_and_drill", new BowAndDrill(new Item.Settings().maxDamage(16)));
    public static final CultivationManual FIRST_MANUAL = register("first_manual", CultivationManual.Builder.create(SpiritLeveling.loc("first_manual"))
            .setLevel(1)
            .addAttributeAndModifier(Elements.WOOD, EntityAttributes.GENERIC_MAX_HEALTH, 1, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeAndModifier(Elements.FIRE, EntityAttributes.GENERIC_MAX_HEALTH, 2, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeAndModifier(Elements.EARTH, EntityAttributes.GENERIC_MAX_HEALTH, 3, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeAndModifier(Elements.METAL, EntityAttributes.GENERIC_MAX_HEALTH, 4, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeAndModifier(Elements.WATER, EntityAttributes.GENERIC_MAX_HEALTH, 5, EntityAttributeModifier.Operation.ADDITION)
            .build(new Item.Settings()));

    // all basic item instances
    public static final Item JADE_CHUNK = register("jade_chunk", new Item(new Item.Settings()));
    public static final Item IMPERIAL_JADE = register("imperial_jade", new Item(new Item.Settings()));
    public static final Item ABRASION_SAND = register("abrasion_sand", new Item(new Item.Settings()));
    public static final Item DRILL = register("drill", new Item(new Item.Settings()));

    // all block item instances
    public static final Item MEDITATION_MAT = AllBlocks.registerBlockItem("meditation_mat", AllBlocks.MEDITATION_MAT);
    public static final Item JADE_STONE_BLOCK = AllBlocks.registerBlockItem("jade_stone_block", AllBlocks.JADE_STONE_BLOCK);
    public static final Item JADE_DEEPSLATE_BLOCK = AllBlocks.registerBlockItem("jade_deepslate_block", AllBlocks.JADE_DEEPSLATE_BLOCK);

    public static final Item INFERIOR_JADE_ENERGY = AllBlocks.registerBlockItem("inferior_jade_energy", AllBlocks.INFERIOR_JADE_ENERGY);
    public static final Item BASIC_JADE_ENERGY = AllBlocks.registerBlockItem("basic_jade_energy", AllBlocks.BASIC_JADE_ENERGY);
    public static final Item LOWER_JADE_ENERGY = AllBlocks.registerBlockItem("lower_jade_energy", AllBlocks.LOWER_JADE_ENERGY);
    public static final Item INTERMEDIATE_JADE_ENERGY = AllBlocks.registerBlockItem("intermediate_jade_energy", AllBlocks.INTERMEDIATE_JADE_ENERGY);
    public static final Item HIGHER_JADE_ENERGY = AllBlocks.registerBlockItem("higher_jade_energy", AllBlocks.HIGHER_JADE_ENERGY);
    public static final Item ADVANCED_JADE_ENERGY = AllBlocks.registerBlockItem("advanced_jade_energy", AllBlocks.ADVANCED_JADE_ENERGY);
    public static final Item SUPERIOR_JADE_ENERGY = AllBlocks.registerBlockItem("superior_jade_energy", AllBlocks.SUPERIOR_JADE_ENERGY);


    // item creative tab
    public static final ItemGroup SPIRIT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPIRIT_PILL))
            .displayName(Text.translatable("itemGroup.spiritleveling.item_group"))
            .entries((context, entries) -> {
                entries.add(SPIRIT_PILL);
                entries.add(S_SPIRIT_PILL);

                entries.add(JADE_CHUNK);
                entries.add(IMPERIAL_JADE);

                entries.add(ABRASION_SAND);
                entries.add(DRILL);
                entries.add(BOW_AND_DRILL);

                entries.add(MEDITATION_MAT);
                entries.add(JADE_STONE_BLOCK);
                entries.add(JADE_DEEPSLATE_BLOCK);
                entries.add(INFERIOR_JADE_ENERGY);
                entries.add(BASIC_JADE_ENERGY);
                entries.add(LOWER_JADE_ENERGY);
                entries.add(INTERMEDIATE_JADE_ENERGY);
                entries.add(HIGHER_JADE_ENERGY);
                entries.add(ADVANCED_JADE_ENERGY);
                entries.add(SUPERIOR_JADE_ENERGY);

                entries.add(FIRST_MANUAL);
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
