package com.rain.spiritleveling.util;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.api.Elements;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;

public class SpiritRegistries {

    public static final RegistryKey<Registry<Elements>> ELEMENTS_KEY = RegistryKey.ofRegistry(SpiritLeveling.loc("registries/elements"));
    public static final Registry<Elements> ELEMENTS = FabricRegistryBuilder.createSimple(ELEMENTS_KEY).buildAndRegister();

    public static void initialize() {
    }
}
