package com.rain.spiritleveling.items;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.items.custom.SpiritPill;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class SpiritItems {

    private SpiritItems() {}

    // all item instances
    public static final SpiritPill SPIRIT_PILL = register("spirit_pill", new SpiritPill(new Item.Settings(), 0));
    public static final SpiritPill S_SPIRIT_PILL = register("s_spirit_pill", new SpiritPill(new Item.Settings(), 4));


    // item creative tab
    public static final ItemGroup SPIRIT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SPIRIT_PILL))
            .displayName(Text.translatable("itemGroup.spirit_leveling.item_group"))
            .entries((context, entries) -> {
                entries.add(SPIRIT_PILL);
                entries.add(S_SPIRIT_PILL);
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
