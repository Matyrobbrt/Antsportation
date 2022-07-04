package com.matyrobbrt.antsportation.data;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface ShapedRecipe {
    ShapedRecipe setResultNBT(CompoundTag tag);
    ShapedRecipe pattern(String... pattern);
    ShapedRecipe define(char key, Ingredient ingredient);
    default ShapedRecipe define(char key, TagKey<Item> tag) {
        return define(key, Ingredient.of(tag));
    }
    default ShapedRecipe define(char key, ItemLike... items) {
        return define(key, Ingredient.of(items));
    }
    @CanIgnoreReturnValue
    ShapedRecipe setId(ResourceLocation id);
    ShapedRecipe group(String group);
}
