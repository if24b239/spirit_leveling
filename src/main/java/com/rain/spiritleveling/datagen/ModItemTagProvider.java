package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.items.AllItems;
import com.rain.spiritleveling.util.SpiritTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(SpiritTags.Items.SPIRIT_CULTIVATION_MANUALS)
                .add(AllItems.FIRST_MANUAL);

        getOrCreateTagBuilder(SpiritTags.Items.MANUAL_FOR_SPIRIT_CONDENSATION)
                .add(AllItems.FIRST_MANUAL);
    }
}
