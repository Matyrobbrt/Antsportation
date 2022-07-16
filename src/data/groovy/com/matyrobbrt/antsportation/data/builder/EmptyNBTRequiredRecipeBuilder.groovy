package com.matyrobbrt.antsportation.data.builder

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.matyrobbrt.antsportation.data.EmptyNBTRequiredRecipe
import com.matyrobbrt.antsportation.registration.AntsportationRecipes
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import org.antlr.v4.runtime.misc.IntegerList
import org.jetbrains.annotations.NotNull

@MethodsReturnNonnullByDefault
class EmptyNBTRequiredRecipeBuilder extends ExtendedShapedRecipeBuilder implements EmptyNBTRequiredRecipe {
    private final IntegerList emptyNBTSlots = new IntegerList()
    EmptyNBTRequiredRecipeBuilder(ItemStack stack) {
        super(stack)
    }

    @Override
    EmptyNBTRequiredRecipe setEmptyNBTSlots(int... slots) {
        emptyNBTSlots.addAll(slots)
        return this
    }

    @Override
    protected ExtendedShapedRecipeBuilder.Result createResult() {
        return new Result(recipeId, this.result, this.count, this.group ?: '', this.rows, this.key, nbt, emptyNBTSlots)
    }

    static class Result extends ExtendedShapedRecipeBuilder.Result {
        private final IntegerList emptyNBTSlots
        Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<String> pPattern, Map<Character, Ingredient> pKey, CompoundTag nbt, IntegerList emptyNBTSlots) {
            super(pId, pResult, pCount, pGroup, pPattern, pKey, nbt)
            this.emptyNBTSlots = emptyNBTSlots
        }

        @Override
        void serializeRecipeData(@NotNull JsonObject pJson) {
            super.serializeRecipeData(pJson)
            final var array = new JsonArray()
            for (int i = 0; i < emptyNBTSlots.size(); i++) {
                array.add(emptyNBTSlots.get(i))
            }
            pJson.add("emptyNbtSlots", array)
        }

        @Override
        RecipeSerializer getType() {
            return AntsportationRecipes.EMPTY_NBT_REQUIRED_SERIALIZER.get()
        }
    }
}
