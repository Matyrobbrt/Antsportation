package com.matyrobbrt.antsportation.data;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface ShapelessRecipe {
    ShapelessRecipe setResultNBT(CompoundTag tag);
    ShapelessRecipe requires(Ingredient ingredient, int quantity);
    default ShapelessRecipe requires(ItemLike item, int quantity) {
        return requires(Ingredient.of(item), quantity);
    }
    default ShapelessRecipe requires(TagKey<Item> tag, int quantity) {
        return requires(Ingredient.of(tag), quantity);
    }
    @CanIgnoreReturnValue
    ShapelessRecipe setId(ResourceLocation id);
    ShapelessRecipe group(String group);
}
