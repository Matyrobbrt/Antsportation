package com.matyrobbrt.antsportation.compat.jei;

import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.renderer.Rect2i;

import java.util.Optional;

record BoxScreenJeiHandler(IIngredientManager manager) implements IGuiContainerHandler<BoxScreen> {

    @Override
    public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(BoxScreen containerScreen, double mouseX, double mouseY) {
        return containerScreen.resolveStack((int)mouseX, (int) mouseY)
                .filter(entry -> !entry.stack().isEmpty())
                .map(e -> new StackIngredient((ITypedIngredient) manager.createTypedIngredient(e.stack()).orElseThrow(), e.area()));
    }

    private record StackIngredient(ITypedIngredient<Object> ingr, Rect2i area) implements IClickableIngredient<Object> {

        @Override
        public ITypedIngredient<Object> getTypedIngredient() {
            return ingr;
        }

        @Override
        public Rect2i getArea() {
            return area;
        }
    }
}
