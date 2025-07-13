package com.rain.spiritleveling.items.custom;

import com.rain.spiritleveling.api.Element;
import com.rain.spiritleveling.api.Stages;
import net.minecraft.item.Item;

public class SpiritIngredientItem extends Item {

    private final Stages ingredientLevel;
    private final Element element;

    public SpiritIngredientItem(Settings settings, Stages ingredient_level, Element element) {
        super(settings);
        ingredientLevel = ingredient_level;
        this.element = element;
    }

    public Stages getIngredientLevel() {
        return ingredientLevel;
    }

    public Element getElement() {
        return element;
    }
}
