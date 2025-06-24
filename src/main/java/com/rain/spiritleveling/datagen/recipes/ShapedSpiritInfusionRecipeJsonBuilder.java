package com.rain.spiritleveling.datagen.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.rain.spiritleveling.items.recipe.ShapedSpiritInfusionRecipe;
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
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ShapedSpiritInfusionRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {

    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    private final Item output;
    private final RecipeCategory category;
    private final Map<Character, Ingredient> inputs = new HashMap<>();

    private int energyCost = 0;
    @Nullable
    private String group;

    public ShapedSpiritInfusionRecipeJsonBuilder(Item output, RecipeCategory category) {
        this.output = output;
        this.category = category;
    }

    public static ShapedSpiritInfusionRecipeJsonBuilder create(Item output, RecipeCategory category) {
        return new ShapedSpiritInfusionRecipeJsonBuilder(output, category);
    }

    public ShapedSpiritInfusionRecipeJsonBuilder woodIngredient(Ingredient input) {
        if (this.inputs.containsKey('w')) {
            throw new IllegalArgumentException("Wood is already defined!");
        } else {
            this.inputs.put('w', input);
            return this;
        }
    }

    public ShapedSpiritInfusionRecipeJsonBuilder fireIngredient(Ingredient input) {
        if (this.inputs.containsKey('f')) {
            throw new IllegalArgumentException("Fire is already defined!");
        } else {
            this.inputs.put('f', input);
            return this;
        }
    }

    public ShapedSpiritInfusionRecipeJsonBuilder earthIngredient(Ingredient input) {
        if (this.inputs.containsKey('e')) {
            throw new IllegalArgumentException("Earth is already defined!");
        } else {
            this.inputs.put('e', input);
            return this;
        }
    }

    public ShapedSpiritInfusionRecipeJsonBuilder metalIngredient(Ingredient input) {
        if (this.inputs.containsKey('m')) {
            throw new IllegalArgumentException("Metal is already defined!");
        } else {
            this.inputs.put('m', input);
            return this;
        }
    }

    public ShapedSpiritInfusionRecipeJsonBuilder waterIngredient(Ingredient input) {
        if (this.inputs.containsKey('a')) {
            throw new IllegalArgumentException("Water is already defined!");
        } else {
            this.inputs.put('a', input);
            return this;
        }
    }

    public ShapedSpiritInfusionRecipeJsonBuilder allIngredients(Map<Character, Item> ingredients) {
        if (!ingredients.containsKey('w') || !ingredients.containsKey('f') || !ingredients.containsKey('e') || !ingredients.containsKey('m') || !ingredients.containsKey('a')) {
            throw new IllegalArgumentException("All keys w, f, e, m and a need to be defined");
        }

        return this.woodIngredient(Ingredient.ofItems(ingredients.get('w').asItem()))
        .fireIngredient(Ingredient.ofItems(ingredients.get('f').asItem()))
        .earthIngredient(Ingredient.ofItems(ingredients.get('e').asItem()))
        .metalIngredient(Ingredient.ofItems(ingredients.get('m').asItem()))
        .waterIngredient(Ingredient.ofItems(ingredients.get('a').asItem()));
    }

    public ShapedSpiritInfusionRecipeJsonBuilder setEnergyCost(int cost) {
        if (this.energyCost != 0) {
            throw new IllegalArgumentException("Energy cost is already defined!");
        } else {
            this.energyCost = cost;
            return this;
        }
    }

    @Override
    public ShapedSpiritInfusionRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        this.advancementBuilder.criterion(name, conditions);
        return this;
    }

    @Override
    public ShapedSpiritInfusionRecipeJsonBuilder group(@Nullable String group) {
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
                new ShapedRecipeJsonProvider(
                        getCraftingCategory(this.category),
                        recipeId,
                        this.output,
                        this.inputs,
                        this.advancementBuilder,
                        recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/"),
                        this.energyCost,
                        this.group == null ? "" : this.group
                )
        );
    }

    private void validate(Identifier recipeId) {
        if (this.energyCost < 0)
            throw new IllegalStateException("Negative Energy Cost not allowed in recipe: '" + recipeId + "'");

        List<Character> tokens = ImmutableList.of('w', 'f', 'e', 'm', 'a');

        boolean atLeastOneIngredient = false;
        for (Character t : tokens) {
            if (inputs.get(t) != Ingredient.ofItems(Items.AIR))
                atLeastOneIngredient = true;
        }

        if (!atLeastOneIngredient)
            throw new IllegalStateException("At least one ingredient for recipe: '" + recipeId + "'");
    }

    static class ShapedRecipeJsonProvider extends RecipeJsonBuilder.CraftingRecipeJsonProvider {

        private final Identifier recipeId;
        private final Item output;
        private final Map<Character, Ingredient> inputs;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;
        private final int cost;
        private final String group;

        protected ShapedRecipeJsonProvider(
                CraftingRecipeCategory craftingCategory,
                Identifier recipeId,
                Item output,
                Map<Character, Ingredient> inputs,
                Advancement.Builder advancementBuilder,
                Identifier advancementId,
                int cost,
                String group
        ) {
            super(craftingCategory);
            this.recipeId = recipeId;
            this.output = output;
            this.inputs = inputs;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.cost = cost;
            this.group = group;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.addProperty("group", this.group);

            JsonObject ingredients = new JsonObject(); // all ingredients

            if (this.inputs.containsKey('w')) {
                ingredients.add("wood", this.inputs.get('w').toJson());
            } else {
                ingredients.add("wood", Ingredient.ofItems(Items.AIR).toJson());
            }

            if (this.inputs.containsKey('f')) {
                ingredients.add("fire", this.inputs.get('f').toJson());
            } else {
                ingredients.add("fire", Ingredient.ofItems(Items.AIR).toJson());
            }

            if (this.inputs.containsKey('e')) {
                ingredients.add("earth", this.inputs.get('e').toJson());
            } else {
                ingredients.add("earth", Ingredient.ofItems(Items.AIR).toJson());
            }

            if (this.inputs.containsKey('m')) {
                ingredients.add("metal", this.inputs.get('m').toJson());
            } else {
                ingredients.add("metal", Ingredient.ofItems(Items.AIR).toJson());
            }

            if (this.inputs.containsKey('a')) {
                ingredients.add("water", this.inputs.get('a').toJson());
            } else {
                ingredients.add("water", Ingredient.ofItems(Items.AIR).toJson());
            }
            ingredients.addProperty("cost", this.cost);

            json.add("ingredients", ingredients);

            json.add("result", Ingredient.ofItems(output).toJson());

        }

        @Override
        public Identifier getRecipeId() {
            return recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ShapedSpiritInfusionRecipe.Serializer.INSTANCE;
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
