package com.matyrobbrt.antsportation.data.builder

import com.google.errorprone.annotations.CanIgnoreReturnValue
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation

import java.util.function.Consumer

interface AntsportationRecipeBuilder {

    void save(Consumer<FinishedRecipe> consumer)
    default void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        setId(recipeId)
        save(consumer)
    }
    @CanIgnoreReturnValue
    AntsportationRecipeBuilder setId(ResourceLocation id)

}
