package com.rain.spiritleveling.datagen.recipes;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SpiritInfusionRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    private final Item output;
    private final RecipeCategory category;
    private final Map<Character, Ingredient> inputs = new HashMap<>();

    private int energyCost = 0;
    private int maxProgress = 100;
    @Nullable
    private String group;

    public SpiritInfusionRecipeJsonBuilder(Item output, RecipeCategory category) {
        this.output = output;
        this.category = category;
    }

    public static SpiritInfusionRecipeJsonBuilder create(Item output, RecipeCategory category) {
        return new SpiritInfusionRecipeJsonBuilder(output, category);
    }

    public SpiritInfusionRecipeJsonBuilder setEnergyCost(int cost) {
        if (this.energyCost != 0) {
            throw new IllegalArgumentException("Energy cost is already defined!");
        } else {
            this.energyCost = cost;
            return this;
        }
    }

    public SpiritInfusionRecipeJsonBuilder setMaxProgress(int max_progress) {
        if (this.maxProgress != 100) {
            throw new IllegalArgumentException("Max Progress can only be set once!");
        } else {
            this.maxProgress = max_progress;
            return this;
        }
    }

    @Override
    public SpiritInfusionRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        this.advancementBuilder.criterion(name, conditions);
        return this;
    }

    @Override
    public SpiritInfusionRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
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
                new ShapedSpiritInfusionRecipeJsonBuilder.ShapedRecipeJsonProvider(
                        getCraftingCategory(this.category),
                        recipeId,
                        this.output,
                        this.inputs,
                        this.advancementBuilder,
                        recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/"),
                        this.energyCost,
                        this.maxProgress,
                        this.group == null ? "" : this.group)
        );
    }
}
