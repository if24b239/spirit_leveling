package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SpiritTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_NETHERITE_TOOL = createTag("needs_netherite_tool");

        public static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, SpiritLeveling.loc(name));
        }
    }

    public static class Items {
        /// These Tags are used for the cultivation manual crafting recipes and should only contain instances of CultivationManual
        /// the MANUAL_FOR_ tags should only contain CultivationManuals with the same CultivationManual.level.
        public static final TagKey<Item> SPIRIT_CULTIVATION_MANUALS = createTag("spirit_cultivation_manuals");
        public static final TagKey<Item> MANUAL_FOR_SPIRIT_CONDENSATION = createTag("manual_for_spirit_condensation");

        public static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, SpiritLeveling.loc(name));
        }
    }
}
