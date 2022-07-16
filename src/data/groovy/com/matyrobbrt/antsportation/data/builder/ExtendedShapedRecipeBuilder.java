package com.matyrobbrt.antsportation.data.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matyrobbrt.antsportation.data.ShapedRecipe;
import com.mojang.serialization.JsonOps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
public class ExtendedShapedRecipeBuilder implements AntsportationRecipeBuilder, ShapedRecipe {
    protected final Item result;
    protected final int count;
    protected CompoundTag nbt;
    protected final List<String> rows = Lists.newArrayList();
    protected ResourceLocation recipeId;
    protected final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    @Nullable
    protected String group;

    public ExtendedShapedRecipeBuilder(ItemStack stack) {
        this.result = stack.getItem();
        this.count = stack.getCount();
        this.nbt = stack.getTag();
        setId(Registry.ITEM.getKey(stack.getItem()));
    }

    /**
     * Adds a key to the recipe pattern.
     */
    @Override
    public ExtendedShapedRecipeBuilder define(char pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
        } else if (pSymbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(pSymbol, pIngredient);
            return this;
        }
    }

    /**
     * Adds a new entry to the patterns for this recipe.
     */
    @Override
    public ExtendedShapedRecipeBuilder pattern(String... patterns) {
        for (String pattern : patterns) {
            definePattern(pattern);
        }
        return this;
    }

    public void definePattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
        }
    }

    @Override
    public ExtendedShapedRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public ExtendedShapedRecipeBuilder setId(ResourceLocation id) {
        this.recipeId = id;
        return this;
    }

    @Override
    public ShapedRecipe setResultNBT(CompoundTag tag) {
        this.nbt = tag;
        return this;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        if (recipeId == null)
            throw new IllegalArgumentException("No recipe ID defined!");
        this.ensureValid(recipeId);
        pFinishedRecipeConsumer.accept(createResult());
    }

    protected Result createResult() {
        return new Result(recipeId, this.result, this.count, this.group == null ? "" : this.group, this.rows, this.key, nbt);
    }

    public void ensureValid(ResourceLocation pId) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + pId + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for(String s : this.rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + pId + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + pId);
            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + pId + " only takes in a single item - should it be a shapeless recipe instead?");
            }
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final CompoundTag nbt;

        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<String> pPattern, Map<Character, Ingredient> pKey, CompoundTag nbt) {
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.group = pGroup;
            this.pattern = pPattern;
            this.key = pKey;
            this.nbt = nbt;
        }

        public void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(String s : this.pattern) {
                jsonarray.add(s);
            }

            pJson.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }

            pJson.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }
            if (nbt != null)
                jsonobject1.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, nbt));

            pJson.add("result", jsonobject1);
        }

        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPED_RECIPE;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
