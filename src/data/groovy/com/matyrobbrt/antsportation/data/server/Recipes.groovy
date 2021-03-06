package com.matyrobbrt.antsportation.data.server

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.compat.patchouli.PatchouliCompat
import com.matyrobbrt.antsportation.data.DatagenHelper
import com.matyrobbrt.antsportation.data.HasRecipe
import com.matyrobbrt.antsportation.data.util.DatagenHelperImpl
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationItems
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import org.jetbrains.annotations.NotNull

import java.util.function.Consumer

class Recipes extends RecipeProvider {
    Recipes(DataGenerator pGenerator) {
        super(pGenerator)
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        final var helper = new DatagenHelperImpl()
        AntsportationItems.ITEMS.getEntries().forEach(it -> {
            final var item = it.get()
            if (item instanceof HasRecipe) {
                item.generateRecipes(helper)
            }
        })
        AntsportationBlocks.BLOCKS.getEntries().forEach(it -> {
            final var block = it.get()
            if (block instanceof HasRecipe) {
                block.generateRecipes(helper)
            }
        })
        addRecipes(helper)
        helper.save(pFinishedRecipeConsumer)
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    private void addRecipes(DatagenHelper helper) {
        helper.shapeless(PatchouliCompat.getBook())
                .requires(Items.BOOK, 1)
                .requires(AntsportationItems.ANT_JAR.get(), 1)
                .setId(Antsportation.rl("guide_book"))
    }
}
