package com.rain.spiritleveling.items.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ShapelessSpiritInfusionRecipe extends SpiritInfusionRecipe implements Recipe<SimpleInventory> {

    protected final TagKey<Item> outputKey;

    public ShapelessSpiritInfusionRecipe(Identifier id, CraftingRecipeCategory category, List<Ingredient> ing, ItemStack out, int cost, int maxProgress, TagKey<Item> outputKey) {
        super(id, category, ing, out, cost, maxProgress);
        this.outputKey = outputKey;
    }

    @Override
    public TagKey<Item> getTag() {
        return outputKey;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;

        ArrayList<ItemStack> notEmptyStacks = new ArrayList<>();

        for (int i = MeditationMatEntity.WOOD_SLOT; i <= MeditationMatEntity.WATER_SLOT; i++) {
            if (inventory.getStack(i).isEmpty())
                continue;

            notEmptyStacks.add(inventory.getStack(i));
        }

        // return false when Inventory has more or less not Empty stacks than the recipe has ingredients
        if (ingredients.size() != notEmptyStacks.size())
            return false;

        // make sure every ingredient matches the inventory
        for (Ingredient i : ingredients) {
            for (ItemStack s : notEmptyStacks) {
                if (i.test(s)) {
                    notEmptyStacks.remove(s);
                    break;
                }
            }
        }

        return notEmptyStacks.isEmpty();
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
        protected TagKey<Item> readTag(JsonObject json) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(JsonHelper.getString(json, "tag")));
        }

        @Override
        protected ArrayList<Ingredient> readIngredients(JsonObject json) {
            ArrayList<Ingredient> recipe_ingredients = new ArrayList<>();
            JsonArray json_ingredients = JsonHelper.getArray(json, "ingredients");

            for (JsonElement j : json_ingredients) {
                recipe_ingredients.add(Ingredient.fromJson(j));
            }

            return recipe_ingredients;
        }

        @Override
        protected TagKey<Item> readTag(PacketByteBuf buf) {
            return TagKey.of(RegistryKeys.ITEM, buf.readIdentifier());
        }

        @Override
        protected void writeTag(PacketByteBuf buf, ShapelessSpiritInfusionRecipe recipe) {
            buf.writeIdentifier(recipe.getTag().id());
        }
    }
}
