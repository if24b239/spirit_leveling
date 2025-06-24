package com.rain.spiritleveling.datagen;

import com.rain.spiritleveling.datagen.recipes.ShapedSpiritInfusionRecipeJsonBuilder;
import com.rain.spiritleveling.items.AllItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    public static List<Item> BOW_AND_DRILL_INGREDIENTS = new ArrayList<>() {
        {
            add(Items.BOW);
            add(AllItems.DRILL);
        }
    };

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // imperial jade
        offerSmithingTableRecipe(exporter, AllItems.BOW_AND_DRILL, AllItems.JADE_CHUNK, AllItems.ABRASION_SAND, RecipeCategory.MISC, AllItems.IMPERIAL_JADE);

        // abrasion sand
        offerAbrasionRecipe(exporter);

        // bow and drill
        offerShapelessInputListRecipe(exporter, AllItems.BOW_AND_DRILL, 1, BOW_AND_DRILL_INGREDIENTS, "tools");

        // drill
        Map<Character, Item> drill_ingredients = Map.of(
                's', Items.STICK,
                'i', Items.IRON_INGOT
        );
        createShapedRecipe(RecipeCategory.TOOLS, drill_ingredients, AllItems.DRILL)
                .pattern("s  ")
                .pattern(" s ")
                .pattern("  i")
                .offerTo(exporter, getItemPath(AllItems.DRILL) + "_ld_shaped");
        createShapedRecipe(RecipeCategory.TOOLS, drill_ingredients, AllItems.DRILL)
                .pattern("  s")
                .pattern(" s ")
                .pattern("i  ")
                .offerTo(exporter, getItemPath(AllItems.DRILL) + "_rd_shaped");
        createShapedRecipe(RecipeCategory.TOOLS, drill_ingredients, AllItems.DRILL)
                .pattern("s")
                .pattern("s")
                .pattern("i")
                .offerTo(exporter, getItemPath(AllItems.DRILL) + "_down_shaped");
        createShapedRecipe(RecipeCategory.TOOLS, drill_ingredients, AllItems.DRILL)
                .pattern("i")
                .pattern("s")
                .pattern("s")
                .offerTo(exporter, getItemPath(AllItems.DRILL) + "_left_shaped");

        // meditation mat
        Map<Character, Item> meditation_mat_ingredients = Map.of(
                'j', AllItems.JADE_CHUNK,
                't', AllItems.BOW_AND_DRILL
        );
        createShapedRecipe(RecipeCategory.MISC, meditation_mat_ingredients, AllItems.MEDITATION_MAT)
                .pattern("jjj")
                .pattern("jtj")
                .pattern("jjj")
                .offerTo(exporter, getItemPath(AllItems.MEDITATION_MAT) + "_shaped");

        // first test infusion recipe
        Map<Character, Item> test_ingredients = Map.of(
                'w', Items.OAK_LOG,
                'f', Items.COAL,
                'e', AllItems.JADE_CHUNK,
                'm', Items.IRON_INGOT,
                'a', Items.WATER_BUCKET
        );
        offerShapedInfusionRecipe(exporter, AllItems.SPIRIT_PILL, test_ingredients, 4, AllItems.MEDITATION_MAT);
    }

    ///
    ///
    /// HELPER FUNCTIONS
    ///
    ///

    private static void offerSmithingTableRecipe(Consumer<RecipeJsonProvider> exporter, Item left, Item input, Item right, RecipeCategory category, Item result) {
        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(left), Ingredient.ofItems(input), Ingredient.ofItems(right), category, result
                )
                .criterion("has_" + getItemPath(input), conditionsFromItem(input))
                .offerTo(exporter, getItemPath(result) + "_smithing");
    }

    private static void offerAbrasionRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, AllItems.ABRASION_SAND, 8)
                .input(Ingredient.fromTag(ItemTags.SAND), 7)
                .input(Items.DIAMOND)
                .input(Items.WATER_BUCKET)
                .criterion("has_" + getItemPath(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter, getItemPath(AllItems.ABRASION_SAND) + "_shapeless");
    }

    private static void offerShapelessInputListRecipe(Consumer<RecipeJsonProvider> exporter, Item output, int count, List<Item> inputs, String group) {
        ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, output, count).group(group);

        assert inputs.size() <= 9;
        for (Item input : inputs) {
            builder.input(input).criterion("has_" + getItemPath(input), conditionsFromItem(input));
        }

        builder.offerTo(exporter, getItemPath(AllItems.BOW_AND_DRILL) + "_shapeless");
    }

    private static ShapedRecipeJsonBuilder createShapedRecipe(RecipeCategory category, Map<Character, Item> ingredients, Item output) {
        ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(category, output, 1);

        assert ingredients.size() <= 9;
        for (Map.Entry<Character, Item> entry : ingredients.entrySet()) {
            builder.input(entry.getKey(), entry.getValue());
            builder.criterion("has_" + getItemPath(entry.getValue()), conditionsFromItem(entry.getValue()));
        }

        return builder;
    }

    private static void offerShapedInfusionRecipe(Consumer<RecipeJsonProvider> exporter, Item output, Map<Character, Item> ingredients, int cost, Item criterion) {
        ShapedSpiritInfusionRecipeJsonBuilder.create(output, RecipeCategory.FOOD)
                .allIngredients(ingredients)
                .setEnergyCost(cost)
                .criterion("has_" + getItemPath(criterion), conditionsFromItem(criterion))
                .offerTo(exporter, getItemPath(output) + "_spirit_infusion");
    }
}
