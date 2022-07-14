package com.matyrobbrt.antsportation.data.server;

import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.data.DatagenHelperImpl;
import com.matyrobbrt.antsportation.data.HasRecipe;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        final var helper = new DatagenHelperImpl();
        AntsportationItems.ITEMS.getEntries().forEach(it -> {
            final var item = it.get();
            if (item instanceof HasRecipe withRecipe) {
                withRecipe.generateRecipes(helper);
            }
        });
        AntsportationBlocks.BLOCKS.getEntries().forEach(it -> {
            final var block = it.get();
            if (block instanceof HasRecipe withRecipe) {
                withRecipe.generateRecipes(helper);
            }
        });
        addRecipes(helper);
        helper.save(pFinishedRecipeConsumer);
    }

    private void addRecipes(DatagenHelper helper) {

    }
}
