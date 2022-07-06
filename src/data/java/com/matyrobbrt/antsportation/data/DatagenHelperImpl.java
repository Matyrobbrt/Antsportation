package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.data.builder.AntsportationRecipeBuilder;
import com.matyrobbrt.antsportation.data.builder.EmptyNBTRequiredRecipeBuilder;
import com.matyrobbrt.antsportation.data.builder.ExtendedShapedRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatagenHelperImpl implements DatagenHelper {
    private final List<AntsportationRecipeBuilder> builders = new ArrayList<>();
    @Override
    public ShapedRecipe shaped(ItemStack result) {
        return create(new ExtendedShapedRecipeBuilder(result));
    }
    public <T extends AntsportationRecipeBuilder> T create(T builder) {
        this.builders.add(builder);
        return builder;
    }
    public void save(Consumer<FinishedRecipe> consumer) {
        builders.forEach(b -> b.save(consumer));
    }

    @Override
    public EmptyNBTRequiredRecipe emptyNBT(ItemStack result) {
        return create(new EmptyNBTRequiredRecipeBuilder(result));
    }
}
