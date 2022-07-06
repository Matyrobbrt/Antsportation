package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.recipe.EmptyNBTRequiredRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class AntsportationRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Antsportation.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registry.RECIPE_SERIALIZER_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EmptyNBTRequiredRecipe>> EMPTY_NBT_REQUIRED_SERIALIZER = SERIALIZERS.register("empty_nbt_required",
            EmptyNBTRequiredRecipe.Serializer::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(String name) {
        return TYPES.register(name, () -> new RecipeType<>() {
            final String regName = Antsportation.rl(name).toString();
            @Override
            public String toString() {
                return regName;
            }
        });
    }
}
