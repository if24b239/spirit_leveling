package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.api.Element;
import com.rain.spiritleveling.api.Stages;
import net.minecraft.item.Item;

public class SpiritIngredientItem extends Item {

    private final Stages INGREDIENT_LEVEL;

    public SpiritIngredientItem(Settings settings, Stages ingredient_level, Element element) {
        super(settings);
        INGREDIENT_LEVEL = ingredient_level;
    }

    public Stages getIngredientLevel() {
        return INGREDIENT_LEVEL;
    }
}
