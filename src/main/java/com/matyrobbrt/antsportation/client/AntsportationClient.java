package com.matyrobbrt.antsportation.client;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.client.blockentity.MarkerRenderer;
import com.matyrobbrt.antsportation.client.entity.AntQueenModel;
import com.matyrobbrt.antsportation.client.entity.AntQueenRenderer;
import com.matyrobbrt.antsportation.client.entity.AntSoldierModel;
import com.matyrobbrt.antsportation.client.entity.AntSoldierRenderer;
import com.matyrobbrt.antsportation.client.screen.BaseContainerScreen;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import com.matyrobbrt.antsportation.client.screen.BoxerScreen;
import com.matyrobbrt.antsportation.client.screen.UnboxerScreen;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
        MenuScreens.register(AntsportationMenus.UNBOXER.get(), UnboxerScreen::new);

        addCustomItemProperties();
        setRenderLayer();
        registerBlockEntityRenderer();
    }

    private static void addCustomItemProperties() {
        ItemProperties.register(AntsportationItems.ANT_JAR.get(), Antsportation.rl("filled"), (stack, level, entity, seed) -> AntJarItem.hasAntInside(stack) ? 1 : 0);
    }

    @SubscribeEvent
    static void registerItemColours(final ColorHandlerEvent.Item event) {
        for (final var tier : BoxItem.BoxTier.values()) {
            event.getItemColors().register((pStack, pTintIndex) -> pTintIndex == 1 ? tier.colour : -1, tier);
        }
    }

    @SubscribeEvent
    public static void registerBlockColours(ColorHandlerEvent.Block event) {
        var blockColors = event.getBlockColors();
        blockColors.register((blockState, tintGetter, blockPos, index) -> {
            var level = Minecraft.getInstance().level;
            var blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof MarkerBE marker) {
                var color = marker.getColor().getMaterialColor().col;
                return color;
            }
            return -1;
        }, AntsportationBlocks.MARKER.get());
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AntQueenModel.LAYER_LOCATION, AntQueenModel::createBodyLayer);
        event.registerLayerDefinition(AntSoldierModel.LAYER_LOCATION, AntSoldierModel::createBodyLayer);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AntsportationEntities.ANT_QUEEN.get(), AntQueenRenderer::new);
        event.registerEntityRenderer(AntsportationEntities.ANT_SOLDIER.get(), AntSoldierRenderer::new);
    }

    public static void renderBg(BaseContainerScreen<?> containerScreen, ResourceLocation texture, PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        final int i = (containerScreen.width - containerScreen.getImageWidth()) / 2;
        final int j = (containerScreen.height - containerScreen.getImageHeight()) / 2;
        containerScreen.blit(poseStack, i, j, 0, 0, containerScreen.getImageWidth(), containerScreen.getImageHeight());
    }

    private static void setRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(AntsportationBlocks.MARKER.get(), RenderType.cutout());
    }

    private static void registerBlockEntityRenderer() {
        MarkerRenderer.register();
    }
}
