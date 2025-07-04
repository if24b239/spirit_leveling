package com.rain.spiritleveling.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rain.spiritleveling.items.recipe.ShapelessSpiritInfusionRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ShapelessSpiritInfusionRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {

    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    private final Item output;
    private final TagKey<Item> outputTag;
    private final RecipeCategory category;
    private final ArrayList<Ingredient> inputs = new ArrayList<>();

    private int energyCost = 0;
    private int maxProgress = 100;
    @Nullable
    private String group;

    public ShapelessSpiritInfusionRecipeJsonBuilder(Item output, RecipeCategory category) {
        this.output = output;
        this.outputTag = null;
        this.category = category;
    }

    public ShapelessSpiritInfusionRecipeJsonBuilder(TagKey<Item> tag, RecipeCategory category) {
        this.output = null;
        this.outputTag = tag;
        this.category = category;
    }

    public static ShapelessSpiritInfusionRecipeJsonBuilder create(Item output, RecipeCategory category) {
        return new ShapelessSpiritInfusionRecipeJsonBuilder(output, category);
    }

    public static ShapelessSpiritInfusionRecipeJsonBuilder create(TagKey<Item> tag, RecipeCategory category) {
        return new ShapelessSpiritInfusionRecipeJsonBuilder(tag, category);
    }

    public ShapelessSpiritInfusionRecipeJsonBuilder addIngredient(Ingredient ing) {
        if (this.inputs.size() >= 5)
            throw new IllegalStateException("Max of 5 inputs allowed");

        this.inputs.add(ing);
        return this;
    }

    public ShapelessSpiritInfusionRecipeJsonBuilder setEnergyCost(int cost) {
        if (this.energyCost != 0) {
            throw new IllegalArgumentException("Energy cost is already defined!");
        }

        this.energyCost = cost;
        return this;
    }

    public ShapelessSpiritInfusionRecipeJsonBuilder setMaxProgress(int max_progress) {
        if (this.maxProgress != 100) {
            throw new IllegalArgumentException("Max Progress can only be set once!");
        }

        this.maxProgress = max_progress;
        return this;
    }

    @Override
    public ShapelessSpiritInfusionRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        this.advancementBuilder.criterion(name, conditions);
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return (this.output == null) ? Items.AIR : this.output;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.advancementBuilder
                .parent(ROOT)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(CriterionMerger.OR);
        exporter.accept(
                new ShapelessRecipeJsonProvider(
                        getCraftingCategory(this.category),
                        recipeId,
                        this.output,
                        this.outputTag,
                        this.inputs,
                        this.advancementBuilder,
                        recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/"),
                        this.energyCost,
                        this.maxProgress,
                        this.group)
        );
    }

    private void validate(Identifier recipeId) {
        if (this.energyCost < 0)
            throw new IllegalStateException("Negative Energy Cost not allowed in recipe: '" + recipeId + "'");

        if (this.maxProgress <= 0)
            throw new IllegalStateException("Max Progress needs to be higher than 0 in recipe: '" + recipeId + "'");

        if (this.inputs.isEmpty())
            throw new IllegalStateException("At least one Ingredient needed in recipe: '" + recipeId + "'");

    }

    static class ShapelessRecipeJsonProvider extends RecipeJsonBuilder.CraftingRecipeJsonProvider {

        private final Identifier recipeId;
        private final Item output;
        private final TagKey<Item> outputTag;
        private final ArrayList<Ingredient> inputs;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;
        private final int cost;
        private final int maxProgress;
        private final String group;

        protected ShapelessRecipeJsonProvider(CraftingRecipeCategory craftingCategory, Identifier recipeId, Item output, TagKey<Item> outputTag, ArrayList<Ingredient> inputs, Advancement.Builder advancementBuilder, Identifier advancementId, int cost, int maxProgress, String group) {
            super(craftingCategory);
            this.recipeId = recipeId;
            this.output = output;
            this.outputTag = outputTag;
            this.inputs = inputs;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.cost = cost;
            this.maxProgress = maxProgress;
            this.group = group;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.addProperty("group", group);

            JsonArray ingredients = new JsonArray();

            for (Ingredient i : inputs) {
                ingredients.add(i.toJson());
            }
            json.add("ingredients", ingredients);

            json.addProperty("cost", this.cost);

            json.addProperty("time", this.maxProgress);

            if (output != null)
                json.add("result", Ingredient.ofItems(output).toJson());

            if (outputTag != null) {
                json.add("output_tag", Ingredient.fromTag(outputTag).toJson());
            }

        }

        @Override
        public Identifier getRecipeId() {
            return recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ShapelessSpiritInfusionRecipe.Serializer.INSTANCE;
        }

        @Override
        public @Nullable JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Override
        public @Nullable Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}