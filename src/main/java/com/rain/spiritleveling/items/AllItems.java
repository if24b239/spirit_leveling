package com.rain.spiritleveling.items;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Element;
import com.rain.spiritleveling.api.Phases;
import com.rain.spiritleveling.api.Stages;
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
import net.puffish.attributesmod.AttributesMod;

import java.util.Arrays;

public class AllItems {

    // custom item instances
    public static final SpiritPill SPIRIT_PILL = register("spirit_pill", new SpiritPill(new Item.Settings(), 0));
    public static final SpiritPill S_SPIRIT_PILL = register("s_spirit_pill", new SpiritPill(new Item.Settings(), 4));
    public static final BowAndDrill BOW_AND_DRILL = register("bow_and_drill", new BowAndDrill(new Item.Settings().maxDamage(16)));

    // cultivation manuals
    public static final CultivationManual SPIRIT_CONDENSATION_BODY_MANUAL = registerBodyManual("spirit_condensation_body_manual", Stages.SPIRIT_CONDENSATION);
    public static final CultivationManual FOUNDATION_BODY_MANUAL = registerBodyManual("foundation_body_manual", Stages.FOUNDATION);
    public static final CultivationManual GOLDEN_CORE_BODY_MANUAL = registerBodyManual("golden_core_body_manual", Stages.GOLDEN_CORE);

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

                entries.add(SPIRIT_CONDENSATION_BODY_MANUAL);
                entries.add(FOUNDATION_BODY_MANUAL);
                entries.add(GOLDEN_CORE_BODY_MANUAL);

                // all elemental items
                Arrays.stream(Element.getItems()).forEach(entries::add);
            })
            .build();

    // helpers to register items
    private static <T extends Item> T register(String path, T item) {

        return Registry.register(Registries.ITEM, SpiritLeveling.loc(path), item);
    }

    private static CultivationManual registerBodyManual(String path, Stages stage) {
        return register(path, CultivationManual.Builder.create(path)
                .setLevel(stage)
                .addAttributeAndModifier(Phases.WOOD, AttributesMod.STAMINA, 0.02 * stage.getValue(), EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeAndModifier(Phases.FIRE, AttributesMod.MAGIC_RESISTANCE, 0.25 * stage.getValue(), EntityAttributeModifier.Operation.ADDITION)
                .addAttributeAndModifier(Phases.METAL, AttributesMod.RANGED_RESISTANCE, 0.25 * stage.getValue(), EntityAttributeModifier.Operation.ADDITION)
                .addAttributeAndModifier(Phases.EARTH, EntityAttributes.GENERIC_MAX_HEALTH, 2 * stage.getValue(), EntityAttributeModifier.Operation.ADDITION)
                .addAttributeAndModifier(Phases.WATER, AttributesMod.MELEE_RESISTANCE, 0.25 * stage.getValue(), EntityAttributeModifier.Operation.ADDITION)
                .build(new Item.Settings()));
    }

    public static void initialize() {
        SpiritLeveling.LOGGER.info("Registering mod Items");

        Registry.register(Registries.ITEM_GROUP, SpiritLeveling.loc("spirit_items_group"), SPIRIT_ITEM_GROUP);
    }
}
