package com.rain.spiritleveling.api;

import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.util.SpiritRegistries;
import net.minecraft.registry.Registry;

public interface Elements {

    Elements WOOD = register("wood");
    Elements FIRE = register("fire");
    Elements EARTH = register("earth");
    Elements METAL = register("metal");
    Elements WATER = register("water");

    static Elements register(String id) {
        return Registry.register(SpiritRegistries.ELEMENTS, SpiritLeveling.loc(id), new Elements() {
            @Override
            public String toString() {
                return id;
            }
        });
    }
}
