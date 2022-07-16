package com.matyrobbrt.antsportation.data.util

import com.matyrobbrt.antsportation.data.DatagenHelper
import com.matyrobbrt.antsportation.data.EmptyNBTRequiredRecipe
import com.matyrobbrt.antsportation.data.ShapedRecipe
import com.matyrobbrt.antsportation.data.ShapelessRecipe
import com.matyrobbrt.antsportation.data.builder.AntsportationRecipeBuilder
import com.matyrobbrt.antsportation.data.builder.EmptyNBTRequiredRecipeBuilder
import com.matyrobbrt.antsportation.data.builder.ExtendedShapedRecipeBuilder
import com.matyrobbrt.antsportation.data.builder.ExtendedShapelessRecipeBuilder
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.ItemStack

import java.util.function.Consumer

class DatagenHelperImpl implements DatagenHelper {
    private final List<AntsportationRecipeBuilder> builders = new ArrayList<>()
    @Override
    ShapedRecipe shaped(ItemStack result) {
        return create(new ExtendedShapedRecipeBuilder(result))
    }
    def <T extends AntsportationRecipeBuilder> T create(T builder) {
        this.builders.add(builder)
        return builder
    }
    void save(Consumer<FinishedRecipe> consumer) {
        builders.forEach(b -> b.save(consumer))
    }

    @Override
    EmptyNBTRequiredRecipe emptyNBT(ItemStack result) {
        return create(new EmptyNBTRequiredRecipeBuilder(result))
    }

    @Override
    ShapelessRecipe shapeless(ItemStack result) {
        return create(new ExtendedShapelessRecipeBuilder(result))
    }
}
