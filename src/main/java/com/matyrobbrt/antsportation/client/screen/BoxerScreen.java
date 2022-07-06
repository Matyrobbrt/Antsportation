package com.matyrobbrt.antsportation.client.screen;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.menu.BoxerMenu;
import com.matyrobbrt.antsportation.network.AntsportationNetwork;
import com.matyrobbrt.antsportation.network.OpenTileContainerPacket;
import com.matyrobbrt.antsportation.network.UpdateBoxerPacket;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoxerScreen extends BaseContainerScreen<BoxerMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = Antsportation.rl("textures/gui/boxer.png");
    public BoxerScreen(BoxerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        titleLabelY += 4;
        inventoryLabelY = 85;
        imageHeight += 13;
        backgroundTexture = CONTAINER_BACKGROUND;
    }

    @Override
    protected void init() {
        super.init();
        final var text = Utils.textComponent("C");
        addRenderableWidget(new Button(leftPos + imageWidth - 7 - 20, topPos + 7, 20, 20, text, b -> {
            onClose();
            AntsportationNetwork.CHANNEL.sendToServer(new OpenTileContainerPacket(menu.tile.getBlockPos(), (byte) 1));
        }, (pButton, pPoseStack, pMouseX, pMouseY) -> renderTooltip(pPoseStack, Translations.CONFIGURATION.translate(), pMouseX, pMouseY)));
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
        this.blit(pPoseStack, this.leftPos + 101, this.topPos + 40, 176, 0, this.menu.getProgressionScaled(), 17);
        this.blit(pPoseStack, this.leftPos + 8, this.topPos + 76, 0, 249, this.menu.getEnergyScaled(), 7);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
        if (pMouseX <= leftPos + 97 && pMouseX >= leftPos + 7 && pMouseY <= topPos + 84 && pMouseY >= topPos + 75) {
            renderTooltip(pPoseStack, Translations.STORED_ENERGY.translate(
                    Utils.textComponent(Utils.getCompressedCount(menu.tile.energy.getEnergyStored()), s -> s.withColor(ChatFormatting.GOLD)),
                    Utils.textComponent(Utils.getCompressedCount(menu.tile.energy.getMaxEnergyStored()), s -> s.withColor(ChatFormatting.AQUA))
            ), pMouseX, pMouseY);
        }

        final var tooltipChild = getChildAt(pMouseX, pMouseY);
        if (tooltipChild.isPresent() && tooltipChild.get() instanceof AbstractWidget wid) {
            wid.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }

    public static final class ConfigurationScreen extends BaseContainerScreen<BoxerMenu.Configuration> {

        private static final ResourceLocation CONTAINER_BACKGROUND = Antsportation.rl("textures/gui/base.png");

        public ConfigurationScreen(BoxerMenu.Configuration pMenu, Inventory pPlayerInventory, Component pTitle) {
            super(pMenu, pPlayerInventory, pTitle);
            backgroundTexture = CONTAINER_BACKGROUND;
        }

        @Override
        protected void init() {
            super.init();
            // TODO the back button needs to be adjusted
            addRenderableWidget(new Button(
                    leftPos + 7, topPos + 7, 50, 20,
                    CommonComponents.GUI_BACK, e -> back()
            ));
            final var releaseWhenBox = new EditBox(minecraft.font, leftPos + 97 + 9, topPos + 11, 50, 20, NarratorChatListener.NO_TITLE);
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
                    .withTooltip(e -> font.split(Translations.BOXER_REDSTONE_CONTROL_TOOLTIP.translate(), 100))
                    .create(leftPos + 97 + 9, topPos + 44, 50, 20, NarratorChatListener.NO_TITLE, (btn, val) -> {
                        AntsportationNetwork.CHANNEL.sendToServer(new UpdateBoxerPacket(
                                menu.tile.getBlockPos(), 1, val.ordinal()
                        ));
                    });
            addRenderableWidget(redstoneModeButton);
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            BoxerScreen.drawString(pPoseStack, minecraft.font, Translations.EJECT_WHEN.translate(), leftPos + 29, topPos + 11 + 5, ChatFormatting.AQUA.getColor());
            BoxerScreen.drawString(pPoseStack, minecraft.font, Translations.REDSTONE_CONTROL.translate(), leftPos + 10, topPos + 44 + 5, ChatFormatting.AQUA.getColor());

            getChildAt(pMouseX, pMouseY).ifPresent(ev -> {
                if (ev instanceof EditBox) {
                    renderTooltip(pPoseStack, minecraft.font.split(Translations.BOXER_EJECT_WHEN_TOOLTIP.translate(), 100), pMouseX, pMouseY);
                } else if (ev instanceof TooltipAccessor ta) {
                    renderTooltip(pPoseStack, ta.getTooltip(), pMouseX, pMouseY);
                }
            });
        }

        private void back() {
            onClose();
            AntsportationNetwork.CHANNEL.sendToServer(new OpenTileContainerPacket(menu.tile.getBlockPos(), null));
        }
    }
}
