package com.rain.spiritleveling.items;

import com.google.common.base.Suppliers;
import com.rain.spiritleveling.SpiritLeveling;
import com.rain.spiritleveling.datagen.recipes.ShapelessSpiritInfusionRecipeJsonBuilder;
import com.rain.spiritleveling.items.recipe.ShapedSpiritInfusionRecipe;
import com.rain.spiritleveling.items.recipe.ShapelessSpiritInfusionRecipe;
import com.rain.spiritleveling.items.recipe.SpiritInfusionRecipe;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class AllRecipes {

    public static void registerRecipes() {
        // Spirit Infusion
        Registry.register(Registries.RECIPE_SERIALIZER, SpiritLeveling.loc(ShapedSpiritInfusionRecipe.Serializer.ID),
                ShapedSpiritInfusionRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, SpiritLeveling.loc(ShapedSpiritInfusionRecipe.Type.ID),
                ShapedSpiritInfusionRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, SpiritLeveling.loc(ShapelessSpiritInfusionRecipe.Serializer.ID),
                ShapelessSpiritInfusionRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, SpiritLeveling.loc(ShapelessSpiritInfusionRecipe.Type.ID),
                ShapelessSpiritInfusionRecipe.Type.INSTANCE);
    }
}
