package com.rain.spiritleveling.items.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.rain.spiritleveling.blocks.entity.MeditationMatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapedSpiritInfusionRecipe implements Recipe<SimpleInventory> {

    private final ItemStack output;
    private final List<Ingredient> ingredients;
    private final Identifier id;
    private final CraftingRecipeCategory category;
    private final int cost;
    private final int maxProgress;

    public ShapedSpiritInfusionRecipe(Identifier id, CraftingRecipeCategory category, List<Ingredient> ing, ItemStack out, int cost, int maxProgress) {
        output = out;
        ingredients = ing;
        this.id = id;
        this.category = category;
        this.cost = cost;
        this.maxProgress = maxProgress;
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
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.ingredients.size());
        list.addAll(ingredients);
        return list;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static Item getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        Item item = Registries.ITEM.getOrEmpty(Identifier.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        } else {
            return item;
        }
    }

    public CraftingRecipeCategory getCategory() {
        return category;
    }

    public int getCost() {
        return cost;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public static class Type implements RecipeType<ShapedSpiritInfusionRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "spirit_infusion_shaped";
    }

    public static class Serializer implements RecipeSerializer<ShapedSpiritInfusionRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "spirit_infusion_shaped";

        @Override
        public ShapedSpiritInfusionRecipe read(Identifier id, JsonObject json) {
            CraftingRecipeCategory category = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(json, "category", null));

            // populate list with minecraft:air ingredients
            ArrayList<Ingredient> recipe_ingredients = new ArrayList<>(Collections.nCopies(5, Ingredient.ofItems(Items.AIR)));
            ItemStack result = new ItemStack(ShapedSpiritInfusionRecipe.getItem(JsonHelper.getObject(json, "result")));

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

            int cost = 0;

            if (JsonHelper.hasElement(json_ingredients, "cost")) {
                cost = JsonHelper.getInt(json_ingredients, "cost");
            }

            int progress = 100;

            if (JsonHelper.hasElement(json, "time")) {
                progress = JsonHelper.getInt(json, "time");
            }

            return new ShapedSpiritInfusionRecipe(id, category, recipe_ingredients, result, cost, progress);
        }

        @Override
        public ShapedSpiritInfusionRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);

            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

            int cost = buf.readInt();

            int progress = buf.readInt();

            ItemStack output = buf.readItemStack();

            return new ShapedSpiritInfusionRecipe(id, category, inputs, output, cost, progress);
        }

        @Override
        public void write(PacketByteBuf buf, ShapedSpiritInfusionRecipe recipe) {
            DefaultedList<Ingredient> list = recipe.getIngredients();

            buf.writeInt(list.size());

            buf.writeEnumConstant(recipe.getCategory());

            for (Ingredient ingredient : list) {
                ingredient.write(buf);
            }

            buf.writeInt(recipe.getCost());

            buf.writeInt(recipe.getMaxProgress());

            buf.writeItemStack(recipe.getOutput(null));
        }
    }
}
