package com.matyrobbrt.antsportation.client.screen;

import com.matyrobbrt.antsportation.client.screen.widget.EnergyUsageWidget;
import com.matyrobbrt.antsportation.client.screen.widget.ProgressWidget;
import com.matyrobbrt.antsportation.menu.boxing.UnboxerMenu;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UnboxerScreen extends BaseContainerScreen<UnboxerMenu> {
    public UnboxerScreen(UnboxerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        titleLabelY += 4;
        inventoryLabelY = 85;
        imageHeight += 13;
        backgroundTexture = BoxerScreen.CONTAINER_BACKGROUND;
    }

    private EnergyUsageWidget energyUsageWidget;

    @Override
    protected void init() {
        super.init();
        addRenderableOnly(new ProgressWidget(this.leftPos + 101, this.topPos + 40, this.menu::getProgressionScaled, true));
        energyUsageWidget = new EnergyUsageWidget(this.leftPos + 7, this.topPos + 75, ServerConfig.CONFIG.boxing().useEnergy()::get, menu.tile.energy::getEnergyStored, menu.tile.energy::getMaxEnergyStored);
    }

    @Override
    protected void renderBg(GuiGraphics pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
        energyUsageWidget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
        energyUsageWidget.attemptTooltipRender(pPoseStack, pMouseX, pMouseY, this);
    }

}
