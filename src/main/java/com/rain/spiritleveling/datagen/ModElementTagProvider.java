package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.api.Element;
import com.rain.spiritleveling.util.SpiritRegistries;
import com.rain.spiritleveling.util.SpiritTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ModElementTagProvider extends FabricTagProvider<Element> {
    public ModElementTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, SpiritRegistries.ELEMENTS_KEY, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder builder = getOrCreateTagBuilder(SpiritTags.Others.ALL_ELEMENTS);

        Arrays.stream(Element.getElements()).forEach(builder::add);
    }
}
