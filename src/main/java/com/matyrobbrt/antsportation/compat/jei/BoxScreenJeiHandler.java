package com.matyrobbrt.antsportation.compat.jei;

import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import org.jetbrains.annotations.Nullable;

class BoxScreenJeiHandler implements IGuiContainerHandler<BoxScreen> {
    @Override
    public @Nullable Object getIngredientUnderMouse(BoxScreen containerScreen, double mouseX, double mouseY) {
        return containerScreen.resolveStack((int) mouseX, (int) mouseY);
    }
}
