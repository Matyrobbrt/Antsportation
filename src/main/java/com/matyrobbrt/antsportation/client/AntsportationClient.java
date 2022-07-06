package com.matyrobbrt.antsportation.client;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.BaseContainerScreen;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import com.matyrobbrt.antsportation.client.screen.BoxerScreen;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Antsportation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AntsportationClient {
    @SubscribeEvent
    static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForgeClient.registerTooltipComponentFactory(BoxItem.Tooltip.class, BoxTooltipClient::new);

        MenuScreens.register(AntsportationMenus.BOX.get(), BoxScreen::new);
        MenuScreens.register(AntsportationMenus.BOXER.get(), BoxerScreen::new);
        MenuScreens.register(AntsportationMenus.BOXER_CONFIGURATION.get(), BoxerScreen.ConfigurationScreen::new);
    }

    public static void renderBg(BaseContainerScreen<?> containerScreen, ResourceLocation texture, PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        final int i = (containerScreen.width - containerScreen.getImageWidth()) / 2;
        final int j = (containerScreen.height - containerScreen.getImageHeight()) / 2;
        containerScreen.blit(poseStack, i, j, 0, 0, containerScreen.getImageWidth(), containerScreen.getImageHeight());
    }
}
