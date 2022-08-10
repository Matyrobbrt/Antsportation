package com.matyrobbrt.antsportation.recipe;

import com.google.gson.JsonObject;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.antlr.v4.runtime.misc.IntegerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class EmptyNBTRequiredRecipe extends ShapedRecipe {
    private static final List<Predicate<ItemStack>> PREDICATES = List.of(
            e -> e.getTag() == null,
            e -> e.getItem() instanceof BoxItem && BoxItem.getStoredCount(e) <= 0
    );

    private final IntegerList emptyNBTSlots;

    public EmptyNBTRequiredRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult, IntegerList emptyNBTSlots) {
        super(pId, pGroup, pWidth, pHeight, pRecipeItems, pResult);
        this.emptyNBTSlots = emptyNBTSlots;
    }

    @Override
    public boolean matches(@NotNull CraftingContainer pCraftingInventory, int pWidth, int pHeight, boolean pMirrored) {
        for (int i = 0; i < pCraftingInventory.getWidth(); ++i) {
            for (int j = 0; j < pCraftingInventory.getHeight(); ++j) {
                final int k = i - pWidth;
                final int l = j - pHeight;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (pMirrored) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                final var index = i + j * pCraftingInventory.getWidth();
                final var stack = pCraftingInventory.getItem(index);

                if (!ingredient.test(stack) || (emptyNBTSlots.contains(index) && PREDICATES.stream().noneMatch(p -> p.test(stack)))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return AntsportationRecipes.EMPTY_NBT_REQUIRED_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public static final class Serializer implements RecipeSerializer<EmptyNBTRequiredRecipe> {

        @Override
        public EmptyNBTRequiredRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            final var shaped = RecipeSerializer.SHAPED_RECIPE.fromJson(pRecipeId, pSerializedRecipe);
            final IntegerList intList = new IntegerList();
            GsonHelper.getAsJsonArray(pSerializedRecipe, "emptyNbtSlots").forEach(e -> intList.add(e.getAsInt()));
            return new EmptyNBTRequiredRecipe(pRecipeId, shaped.getGroup(), shaped.getWidth(), shaped.getHeight(), shaped.recipeItems, shaped.getResultItem(), intList);
        }

        @Override
        public EmptyNBTRequiredRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int i = pBuffer.readVarInt();
            int j = pBuffer.readVarInt();
            String s = pBuffer.readUtf();
            NonNullList<Ingredient> items = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < items.size(); ++k) {
                items.set(k, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack itemstack = pBuffer.readItem();
            IntegerList intList = new IntegerList();
            final var amount = pBuffer.readByte();
            for (var x = 0; x < amount; x++) {
                intList.add(pBuffer.readByte());
            }
            return new EmptyNBTRequiredRecipe(pRecipeId, s, i, j, items, itemstack, intList);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, EmptyNBTRequiredRecipe pRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
            pBuffer.writeByte(pRecipe.emptyNBTSlots.size());
            for (int i = 0; i < pRecipe.emptyNBTSlots.size(); i++) {
                pBuffer.writeByte(pRecipe.emptyNBTSlots.get(i));
            }
        }
    }
}
