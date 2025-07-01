package com.rain.spiritleveling.items.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapelessSpiritInfusionRecipe extends SpiritInfusionRecipe implements Recipe<SimpleInventory> {

    public ShapelessSpiritInfusionRecipe(Identifier id, CraftingRecipeCategory category, List<Ingredient> ing, ItemStack out, int cost, int maxProgress) {
        super(id, category, ing, out, cost, maxProgress);
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShapelessSpiritInfusionRecipe> {
        public static final ShapelessSpiritInfusionRecipe.Type INSTANCE = new ShapelessSpiritInfusionRecipe.Type();
        public static final String ID = "spirit_infusion_shapeless";
    }

    public static class Serializer extends SpiritInfusionRecipe.Serializer<ShapelessSpiritInfusionRecipe> {

        public static final ShapelessSpiritInfusionRecipe.Serializer INSTANCE = new ShapelessSpiritInfusionRecipe.Serializer(ShapelessSpiritInfusionRecipe::new);
        public static final String ID = "spirit_infusion_shapeless";

        public Serializer(Factory<ShapelessSpiritInfusionRecipe> factory) {
            super(factory);
        }

        @Override
        public ShapelessSpiritInfusionRecipe read(Identifier id, JsonObject json) {
            CraftingRecipeCategory category = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(json, "category", null));

            ArrayList<Ingredient> recipe_ingredients = new ArrayList<>();
            ItemStack result = new ItemStack(ShapedSpiritInfusionRecipe.getItem(JsonHelper.getObject(json, "result")));

            JsonArray json_ingredients = JsonHelper.getArray(json, "ingredients");

            for (JsonElement j : json_ingredients) {
                recipe_ingredients.add(Ingredient.fromJson(j));
            }

            int cost = 0;

            if (JsonHelper.hasElement(json, "cost")) {
                cost = JsonHelper.getInt(json, "cost");
            }

            int progress = 100;

            if (JsonHelper.hasElement(json, "time")) {
                progress = JsonHelper.getInt(json, "time");
            }

            return new ShapelessSpiritInfusionRecipe(id, category, recipe_ingredients, result, cost, progress);
        }

    }
}
