package com.matyrobbrt.antsportation.client.screen;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.widget.EnergyUsageWidget;
import com.matyrobbrt.antsportation.client.screen.widget.ProgressWidget;
import com.matyrobbrt.antsportation.menu.boxing.BoxerMenu;
import com.matyrobbrt.antsportation.network.AntsportationNetwork;
import com.matyrobbrt.antsportation.network.OpenTileContainerPacket;
import com.matyrobbrt.antsportation.network.RequestUpdatePacket;
import com.matyrobbrt.antsportation.network.UpdateBoxerPacket;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoxerScreen extends BaseContainerScreen<BoxerMenu> {
    public static final ResourceLocation CONTAINER_BACKGROUND = Antsportation.rl("textures/gui/boxing_machine_base.png");
    public BoxerScreen(BoxerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        titleLabelY += 4;
        inventoryLabelY = 85;
        imageHeight += 13;
        backgroundTexture = CONTAINER_BACKGROUND;
    }

    private EnergyUsageWidget energyUsageWidget;

    @Override
    protected void init() {
        super.init();
        final var text = Utils.textComponent("C");
        addRenderableWidget(Button.builder(text, b -> {
            onClose();
            AntsportationNetwork.CHANNEL.sendToServer(new OpenTileContainerPacket(menu.tile.getBlockPos(), (byte) 1));
        }).pos(leftPos + imageWidth - 7 - 20, topPos + 7).size(20, 20).tooltip(Tooltip.create(Translations.CONFIGURATION.translate())).build());
        addRenderableOnly(new ProgressWidget(this.leftPos + 101, this.topPos + 40, this.menu::getProgressionScaled, false));
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

    public static final class ConfigurationScreen extends BaseContainerScreen<BoxerMenu.Configuration> {

        private static final ResourceLocation CONTAINER_BACKGROUND = Antsportation.rl("textures/gui/base.png");

        public ConfigurationScreen(BoxerMenu.Configuration pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle);
            backgroundTexture = CONTAINER_BACKGROUND;
            AntsportationNetwork.CHANNEL.sendToServer(RequestUpdatePacket.INSTANCE);
        }

        @Override
        protected void init() {
            super.init();
            addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, e -> back())
                    .pos(leftPos + imageWidth - 7 - 36, topPos + 83 - 15)
                    .size(36, 15).build());
            final var releaseWhenBox = new EditBox(minecraft.font, leftPos + 97 + 9, topPos + 11 + 5, 50, 20, GameNarrator.NO_TITLE);
            releaseWhenBox.setValue(String.valueOf(menu.tile.releasePercent));
            releaseWhenBox.setResponder(v -> {
                try {
                    final var count = Integer.parseInt(v);
                    if (count > 100 || count < 0) {
                        releaseWhenBox.setTextColor(16711680);
                    } else {
                        menu.tile.releasePercent = count;
                        AntsportationNetwork.CHANNEL.sendToServer(new UpdateBoxerPacket(
                                menu.tile.getBlockPos(), 0, count
                        ));
                        releaseWhenBox.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
                    }
                } catch (Exception e) {
                    releaseWhenBox.setTextColor(16711680);
                }
            });
            addRenderableWidget(releaseWhenBox);
            final var redstoneModeButton = CycleButton.builder(RedstoneControl::getName)
                    .displayOnlyValue()
                    .withValues(RedstoneControl.values())
                    .withInitialValue(menu.tile.redstoneControl)
                    .withTooltip(e -> Tooltip.create(Translations.BOXER_REDSTONE_CONTROL_TOOLTIP.translate()))
                    .create(leftPos + 97 + 9, topPos + 44, 50, 20, GameNarrator.NO_TITLE, (btn, val) -> {
                        AntsportationNetwork.CHANNEL.sendToServer(new UpdateBoxerPacket(
                                menu.tile.getBlockPos(), 1, val.ordinal()
                        ));
                    });
            addRenderableWidget(redstoneModeButton);
        }

        @Override
        public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            pPoseStack.drawString(minecraft.font, Translations.EJECT_WHEN.translate(), leftPos + 29, topPos + 11 + 5 + 5, ChatFormatting.AQUA.getColor());
            pPoseStack.drawString(minecraft.font, Translations.REDSTONE_CONTROL.translate(), leftPos + 10, topPos + 44 + 5, ChatFormatting.AQUA.getColor());

            getChildAt(pMouseX, pMouseY).ifPresent(ev -> {
                if (ev instanceof EditBox) {
                    pPoseStack.renderTooltip(minecraft.font, minecraft.font.split(Translations.BOXER_EJECT_WHEN_TOOLTIP.translate(), 100), pMouseX, pMouseY);
                }
            });
        }

        private void back() {
            onClose();
            AntsportationNetwork.CHANNEL.sendToServer(new OpenTileContainerPacket(menu.tile.getBlockPos(), null));
        }
    }
}
