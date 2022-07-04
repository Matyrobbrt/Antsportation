package com.matyrobbrt.antsportation.jei;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
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
}
