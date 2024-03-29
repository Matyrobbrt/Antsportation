package com.matyrobbrt.antsportation.client;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.BoxModel;
import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.client.blockentity.AntJarRenderer;
import com.matyrobbrt.antsportation.client.entity.AntQueenModel;
import com.matyrobbrt.antsportation.client.entity.AntQueenRenderer;
import com.matyrobbrt.antsportation.client.entity.AntSoldierModel;
import com.matyrobbrt.antsportation.client.entity.AntSoldierRenderer;
import com.matyrobbrt.antsportation.client.entity.AntWorkerModel;
import com.matyrobbrt.antsportation.client.entity.AntWorkerRenderer;
import com.matyrobbrt.antsportation.client.screen.BaseContainerScreen;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import com.matyrobbrt.antsportation.client.screen.BoxerScreen;
import com.matyrobbrt.antsportation.client.screen.UnboxerScreen;
import com.matyrobbrt.antsportation.client.tooltip.BoxTooltipClient;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.config.ClientConfig;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Antsportation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AntsportationClient {

    @SubscribeEvent
    static void clientSetup(final FMLClientSetupEvent event) {
        registerMenus();
        addCustomItemProperties();
        setRenderLayer();
        registerBlockEntityRenderer();

        MinecraftForge.EVENT_BUS.addListener(AntsportationClient::onTooltip);
    }

    @SubscribeEvent
    static void registerTooltipComponentFactory(final RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(BoxItem.Tooltip.class, BoxTooltipClient::new);
    }

    private static void registerBlockEntityRenderer() {
        BlockEntityRenderers.register(AntsportationBlocks.ANT_JAR_BE.get(), $ -> new AntJarRenderer());
    }

    private static void registerMenus() {
        MenuScreens.register(AntsportationMenus.BOX.get(), BoxScreen::new);
        MenuScreens.register(AntsportationMenus.BOXER.get(), BoxerScreen::new);
        MenuScreens.register(AntsportationMenus.BOXER_CONFIGURATION.get(), BoxerScreen.ConfigurationScreen::new);
        MenuScreens.register(AntsportationMenus.UNBOXER.get(), UnboxerScreen::new);
    }

    private static void addCustomItemProperties() {
        ItemProperties.register(AntsportationItems.ANT_JAR.get(), Antsportation.rl("filled"), (stack, level, entity, seed) -> AntJarItem.hasAntInside(stack) ? 1 : 0);
    }

    @SubscribeEvent
    static void registerBlockColours(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, level, blockPos, index) -> {
            if (level == null || blockPos == null) return -1;
            final var blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof MarkerBE marker) {
                return marker.getColor().getMapColor().col;
            }
            return -1;
        }, AntsportationBlocks.MARKER.get(), AntsportationBlocks.CHUNK_LOADING_MARKER.get());
    }

    @SubscribeEvent
    static void registerItemColours(final RegisterColorHandlersEvent.Item event) {
        for (final var tier : BoxItem.BoxTier.values()) {
            event.register((pStack, pTintIndex) -> pTintIndex == 1 ? tier.colour : -1, tier);
        }
    }

    @SubscribeEvent
    static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AntQueenModel.LAYER_LOCATION, AntQueenModel::createBodyLayer);
        event.registerLayerDefinition(AntSoldierModel.LAYER_LOCATION, AntSoldierModel::createBodyLayer);
        event.registerLayerDefinition(AntWorkerModel.LAYER_LOCATION, AntWorkerModel::createBodyLayer);
        event.registerLayerDefinition(AntWorkerModel.LAYER_LOCATION_BOX, BoxModel::createBoxLayer);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AntsportationEntities.ANT_QUEEN.get(), AntQueenRenderer::new);
        event.registerEntityRenderer(AntsportationEntities.ANT_WORKER.get(), AntWorkerRenderer::new);

        event.registerEntityRenderer(AntsportationEntities.ANT_SOLDIER.get(), AntSoldierRenderer::new);
        event.registerEntityRenderer(AntsportationEntities.HILL_ANT_SOLDIER.get(), AntSoldierRenderer::new);
    }

    static void onTooltip(final ItemTooltipEvent event) {
        if (ServerConfig.CONFIG.ants().onlyTransportableItems().get() && ClientConfig.CONFIG.showTransportableItems().get() && event.getItemStack().is(AntsportationTags.Items.ANT_TRANSPORTABLE)) {
            event.getToolTip().add(Translations.TRANSPORTABLE_ITEM.translate());
        }
    }

    public static void renderBg(BaseContainerScreen<?> containerScreen, ResourceLocation texture, GuiGraphics poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        final int i = (containerScreen.width - containerScreen.getImageWidth()) / 2;
        final int j = (containerScreen.height - containerScreen.getImageHeight()) / 2;
        poseStack.blit(texture, i, j, 0, 0, containerScreen.getImageWidth(), containerScreen.getImageHeight());
    }

    private static void setRenderLayer() {
        // TODO render layer in json
        ItemBlockRenderTypes.setRenderLayer(AntsportationBlocks.MARKER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AntsportationBlocks.CHUNK_LOADING_MARKER.get(), RenderType.cutout());
    }

}
