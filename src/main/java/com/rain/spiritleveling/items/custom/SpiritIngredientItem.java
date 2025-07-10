package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.api.Elements;
import com.rain.spiritleveling.api.Stages;
import net.minecraft.item.Item;

public class SpiritIngredientItem extends Item {

    private final Stages INGREDIENT_LEVEL;
    private final Elements ELEMENT;

    public SpiritIngredientItem(Settings settings, Stages ingredient_level, Elements element) {
        super(settings);
        INGREDIENT_LEVEL = ingredient_level;
        ELEMENT = element;
    }

    public Stages getIngredientLevel() {
        return INGREDIENT_LEVEL;
    }
}
