package com.matyrobbrt.antsportation.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface DatagenHelper {
    ShapedRecipe shaped(ItemStack result);
    default ShapedRecipe shaped(Item result, int count) {
        return shaped(new ItemStack(result, count));
    }
    default ShapedRecipe shaped(Item result) {
        return shaped(result, 1);
    }
}
