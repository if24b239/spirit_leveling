package com.rain.spiritleveling.items.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BowAndDrill extends Item {
    public BowAndDrill(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamage(stack.getDamage() + 1);

        if (copy.getDamage() >= copy.getMaxDamage())
            return ItemStack.EMPTY;

        return copy;
    }
}
