package com.matyrobbrt.antsportation.compat.jei;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;

@JeiPlugin
@SuppressWarnings("unused")
public class AntsportationJei implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(Antsportation.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(BoxScreen.class, new BoxScreenJeiHandler());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, AntsportationItems.ANT_JAR.get(), (ingredient, context) -> {
            if (ingredient.isEmpty() || context == UidContext.Recipe)
                return IIngredientSubtypeInterpreter.NONE;
            return AntJarItem.hasAntInside(ingredient) ? "antinside" : IIngredientSubtypeInterpreter.NONE;
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var ingredientManager = registration.getIngredientManager();
        registerInfos(registration);
    }

    private void registerInfos(IRecipeRegistration registration) {
        AntsportationItems.ITEMS.getEntries().forEach(itemReg -> {
            final var it = itemReg.get();
            JEIInfoProvider provider = null;
            if (it instanceof BlockItem bi && bi.getBlock() instanceof JEIInfoProvider infoProvider) {
                provider = infoProvider;
            } else if (it instanceof JEIInfoProvider provider1) {
                provider = provider1;
            }
            if (provider != null) {
                registration.addIngredientInfo(it.getDefaultInstance(), VanillaTypes.ITEM_STACK, provider.getInfo().toArray(Component[]::new));
            }
        });
    }
}
