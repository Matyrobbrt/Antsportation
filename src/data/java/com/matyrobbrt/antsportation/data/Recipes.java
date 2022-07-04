package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.item.BaseItem;
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
            if (item instanceof BaseItem base) {
                base.generateRecipes(helper);
            }
        });
        addRecipes(helper);
        helper.save(pFinishedRecipeConsumer);
    }

    private void addRecipes(DatagenHelper helper) {

    }
}
