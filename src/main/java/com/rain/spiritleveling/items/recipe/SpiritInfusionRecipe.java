package com.rain.spiritleveling.items.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public abstract class SpiritInfusionRecipe implements Recipe<SimpleInventory> {
    protected final ItemStack output;
    protected final List<Ingredient> ingredients;
    protected final Identifier id;
    protected final CraftingRecipeCategory category;
    protected final int cost;
    protected final int maxProgress;

    public SpiritInfusionRecipe(Identifier id, CraftingRecipeCategory category, List<Ingredient> ing, ItemStack out, int cost, int maxProgress) {
        output = out;
        ingredients = ing;
        this.id = id;
        this.category = category;
        this.cost = cost;
        this.maxProgress = maxProgress;
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

    public CraftingRecipeCategory getCategory() {
        return category;
    }

    public int getCost() {
        return cost;
    }

    public int getMaxProgress() {
        return maxProgress;
    }


    public static class Serializer<T extends SpiritInfusionRecipe> implements RecipeSerializer<T> {
        private final Factory<T> factory;

        public Serializer(Factory<T> factory) {
            this.factory = factory;
        }

        public interface Factory<T extends SpiritInfusionRecipe> {
            T create(Identifier id, CraftingRecipeCategory category, DefaultedList<Ingredient> inputs,
                     ItemStack output, int cost, int progress);
        }

        @Override
        public T read(Identifier id, JsonObject json) {
            return null;
        }

        @Override
        public T read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            CraftingRecipeCategory category = buf.readEnumConstant(CraftingRecipeCategory.class);

            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

            int cost = buf.readInt();

            int progress = buf.readInt();

            ItemStack output = buf.readItemStack();

            return this.factory.create(id, category, inputs, output, cost, progress);
        }

        @Override
        public void write(PacketByteBuf buf, T recipe) {
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
