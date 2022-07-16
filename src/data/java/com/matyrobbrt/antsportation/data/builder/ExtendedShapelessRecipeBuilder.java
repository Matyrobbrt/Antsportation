package com.matyrobbrt.antsportation.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matyrobbrt.antsportation.data.ShapelessRecipe;
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
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
public class ExtendedShapelessRecipeBuilder implements AntsportationRecipeBuilder, ShapelessRecipe {
    private final Item result;
    private final int count;
    @Nullable
    private CompoundTag nbt;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    @Nullable
    private String group;
    private ResourceLocation recipeId;

    public ExtendedShapelessRecipeBuilder(ItemStack stack) {
        this.result = stack.getItem();
        this.count = stack.getCount();
        this.nbt = stack.getTag();
        setId(Registry.ITEM.getKey(stack.getItem()));
    }

    @Override
    public ShapelessRecipe requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredient);
        }
        return this;
    }

    @Override
    public ExtendedShapelessRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public ExtendedShapelessRecipeBuilder setId(ResourceLocation id) {
        this.recipeId = id;
        return this;
    }

    @Override
    public ShapelessRecipe setResultNBT(CompoundTag tag) {
        this.nbt = tag;
        return this;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        if (recipeId == null)
            throw new IllegalArgumentException("No recipe ID defined!");
        pFinishedRecipeConsumer.accept(createResult());
    }

    protected Result createResult() {
        return new Result(recipeId, this.result, this.count, this.group == null ? "" : this.group, ingredients, nbt);
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final CompoundTag tag;

        public Result(ResourceLocation pId, Item pResult, int pCount, String pGroup, List<Ingredient> pIngredients, CompoundTag tag) {
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.group = pGroup;
            this.ingredients = pIngredients;
            this.tag = tag;
        }

        public void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            pJson.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (tag != null)
                jsonobject.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.COMPRESSED, tag));
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            pJson.add("result", jsonobject);
        }

        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
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
