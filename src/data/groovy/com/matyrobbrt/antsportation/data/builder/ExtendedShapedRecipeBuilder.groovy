package com.matyrobbrt.antsportation.data.builder

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.matyrobbrt.antsportation.data.ShapedRecipe
import com.mojang.serialization.JsonOps
import net.minecraft.MethodsReturnNonnullByDefault
import net.minecraft.core.Registry
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import org.jetbrains.annotations.NotNull

import javax.annotation.Nullable
import java.util.function.Consumer

@MethodsReturnNonnullByDefault
class ExtendedShapedRecipeBuilder implements AntsportationRecipeBuilder, ShapedRecipe {
    protected final Item result
    protected final int count
    protected CompoundTag nbt
    protected final List<String> rows = Lists.newArrayList()
    protected ResourceLocation recipeId
    protected final Map<Character, Ingredient> key = Maps.newLinkedHashMap()
    @Nullable
    protected String group

    ExtendedShapedRecipeBuilder(ItemStack stack) {
        this.result = stack.getItem()
        this.count = stack.getCount()
        this.nbt = stack.getTag()
        setId(Registry.ITEM.getKey(stack.getItem()))
    }

    /**
     * Adds a key to the recipe pattern.
     */
    @Override
    ExtendedShapedRecipeBuilder define(char pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '$pSymbol' is already defined!")
        } else if (pSymbol == ' ' as char) {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined")
        } else {
            this.key.put(pSymbol, pIngredient)
            return this
        }
    }

    /**
     * Adds a new entry to the patterns for this recipe.
     */
    @Override
    ExtendedShapedRecipeBuilder pattern(String... patterns) {
        for (String pattern : patterns) {
            definePattern(pattern)
        }
        return this
    }

    void definePattern(String pattern) {
        if (rows && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!")
        } else {
            this.rows.add(pattern)
        }
    }

    @Override
    ExtendedShapedRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName
        return this
    }

    @Override
    ExtendedShapedRecipeBuilder setId(ResourceLocation id) {
        this.recipeId = id
        return this
    }

    @Override
    ShapedRecipe setResultNBT(CompoundTag tag) {
        this.nbt = tag
        return this
    }

    @Override
    void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        if (!recipeId)
            throw new IllegalArgumentException("No recipe ID defined!")
        this.ensureValid(recipeId)
        pFinishedRecipeConsumer.accept(createResult())
    }

    protected Result createResult() {
        return new Result(recipeId, this.result, this.count, this.group ?: '', this.rows, this.key, nbt)
    }

    void ensureValid(ResourceLocation pId) {
        if (!rows) {
            throw new IllegalStateException("No pattern is defined for shaped recipe $pId!")
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet())
            set.remove(' ')

            for (final s in this.rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i)
                    if (c0 !in key && c0 != ' ' as char) {
                        throw new IllegalStateException("Pattern in recipe $pId uses undefined symbol '$c0'")
                    }

                    set.remove(c0)
                }
            }

            if (set) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe $pId")
            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe $pId only takes in a single item - should it be a shapeless recipe instead?")
            }
        }
    }

    static class Result implements FinishedRecipe {
        private final ResourceLocation id
        private final Item result
        private final int count
        private final String group
        private final List<String> pattern
        private final Map<Character, Ingredient> key
        private final CompoundTag nbt

        Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<String> pPattern, Map<Character, Ingredient> pKey, CompoundTag nbt) {
            this.id = pId
            this.result = pResult
            this.count = pCount
            this.group = pGroup
            this.pattern = pPattern
            this.key = pKey
            this.nbt = nbt
        }

        void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group)
            }

            JsonArray jsonarray = new JsonArray()

            for (final s in this.pattern) {
                jsonarray.add(s)
            }

            pJson.add("pattern", jsonarray)
            JsonObject jsonobject = new JsonObject()

            for (final entry in this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson())
            }

            pJson.add("key", jsonobject)
            JsonObject jsonobject1 = new JsonObject()
            jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString())
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count)
            }
            if (nbt != null)
                jsonobject1.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, nbt))

            pJson.add("result", jsonobject1)
        }

        RecipeSerializer getType() {
            RecipeSerializer.SHAPED_RECIPE
        }

        ResourceLocation getId() {
            return this.id
        }

        @Nullable
        JsonObject serializeAdvancement() {
            return null
        }

        @Nullable
        ResourceLocation getAdvancementId() {
            return null
        }
    }
}
