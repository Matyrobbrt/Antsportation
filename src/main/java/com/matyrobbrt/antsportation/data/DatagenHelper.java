package com.matyrobbrt.antsportation.data;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface DatagenHelper {
    ShapedRecipe shaped(ItemStack result);
    default ShapedRecipe shaped(ItemLike result, int count) {
        return shaped(new ItemStack(result, count));
    }
    default ShapedRecipe shaped(ItemLike result) {
        return shaped(result, 1);
    }

    EmptyNBTRequiredRecipe emptyNBT(ItemStack result);
    default EmptyNBTRequiredRecipe emptyNBT(ItemLike result, int count) {
        return emptyNBT(new ItemStack(result, count));
    }
    default EmptyNBTRequiredRecipe emptyNBT(ItemLike result) {
        return emptyNBT(result, 1);
    }
}
