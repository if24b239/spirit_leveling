package com.rain.spiritleveling.items.custom;

import net.minecraft.item.Item;

public class SpiritIngredientItem extends Item {

    private final int INGREDIENT_LEVEL;

    public SpiritIngredientItem(Settings settings, int ingredient_level) {
        super(settings);
        INGREDIENT_LEVEL = ingredient_level;
    }

    public int getIngredientLevel() {
        return INGREDIENT_LEVEL;
    }
}
