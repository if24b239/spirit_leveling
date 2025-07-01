package com.rain.spiritleveling.items.recipe;

import com.google.gson.JsonObject;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapedSpiritInfusionRecipe extends SpiritInfusionRecipe implements Recipe<SimpleInventory> {

    public ShapedSpiritInfusionRecipe(Identifier id, CraftingRecipeCategory category, List<Ingredient> ing, ItemStack out, int cost, int maxProgress) {
        super(id, category, ing, out, cost, maxProgress);
    }


    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;

        boolean woodMatches = (ingredients.get(0).isEmpty()) ? inventory.getStack(MeditationMatEntity.WOOD_SLOT).isEmpty() : ingredients.get(0).test(inventory.getStack(MeditationMatEntity.WOOD_SLOT));
        boolean fireMatches = (ingredients.get(1).isEmpty()) ? inventory.getStack(MeditationMatEntity.FIRE_SLOT).isEmpty() : ingredients.get(1).test(inventory.getStack(MeditationMatEntity.FIRE_SLOT));
        boolean earthMatches = (ingredients.get(2).isEmpty()) ? inventory.getStack(MeditationMatEntity.EARTH_SLOT).isEmpty() : ingredients.get(2).test(inventory.getStack(MeditationMatEntity.EARTH_SLOT));
        boolean metalMatches = (ingredients.get(3).isEmpty()) ? inventory.getStack(MeditationMatEntity.METAL_SLOT).isEmpty() : ingredients.get(3).test(inventory.getStack(MeditationMatEntity.METAL_SLOT));
        boolean waterMatches = (ingredients.get(4).isEmpty()) ? inventory.getStack(MeditationMatEntity.WATER_SLOT).isEmpty() : ingredients.get(4).test(inventory.getStack(MeditationMatEntity.WATER_SLOT));


        return woodMatches && fireMatches && earthMatches && metalMatches && waterMatches;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShapedSpiritInfusionRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "spirit_infusion_shaped";
    }

    public static class Serializer extends SpiritInfusionRecipe.Serializer<ShapedSpiritInfusionRecipe> {
        public static final Serializer INSTANCE = new Serializer(ShapedSpiritInfusionRecipe::new);
        public static final String ID = "spirit_infusion_shaped";

        public Serializer(Factory<ShapedSpiritInfusionRecipe> factory) {
            super(factory);
        }

        @Override
        protected ArrayList<Ingredient> readIngredients(JsonObject json) {
            ArrayList<Ingredient> recipe_ingredients = new ArrayList<>(Collections.nCopies(5, Ingredient.ofItems(Items.AIR)));
            JsonObject json_ingredients = JsonHelper.getObject(json, "ingredients");

            if (JsonHelper.hasElement(json_ingredients, "wood")) {
                Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(json_ingredients, "wood"), true);
                recipe_ingredients.set(0, ingredient);
            }

            if (JsonHelper.hasElement(json_ingredients, "fire")) {
                Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(json_ingredients, "fire"), true);
                recipe_ingredients.set(1, ingredient);
            }

            if (JsonHelper.hasElement(json_ingredients, "earth")) {
                Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(json_ingredients, "earth"), true);
                recipe_ingredients.set(2, ingredient);
            }

            if (JsonHelper.hasElement(json_ingredients, "metal")) {
                Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(json_ingredients, "metal"), true);
                recipe_ingredients.set(3, ingredient);
            }

            if (JsonHelper.hasElement(json_ingredients, "water")) {
                Ingredient ingredient = Ingredient.fromJson(JsonHelper.getElement(json_ingredients, "water"), true);
                recipe_ingredients.set(4, ingredient);
            }

            return recipe_ingredients;
        }
    }
}
