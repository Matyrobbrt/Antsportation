package com.matyrobbrt.antsportation.client;

import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public final class BoxTooltipClient implements ClientTooltipComponent {
    private final BoxItem.Tooltip tooltip;
    private final List<FormattedCharSequence> contentsTooltips = new ArrayList<>();
    private final int maxWidth;

    public static final int MAX_TOOLTIP_LENGTH = 5;
    public static final int MAX_STACK_TOOLTIP_LENGTH = 150;
    public static final int MAX_SHOWN_STACKS = 10;

    public BoxTooltipClient(BoxItem.Tooltip tooltip) {
        this.tooltip = tooltip;
        contentsTooltips.add((tooltip.stacks().isEmpty() ? Translations.EMPTY : Translations.CONTENTS).translate().getVisualOrderText());
        for (int i = 0; i < Math.min(MAX_SHOWN_STACKS, tooltip.stacks().size()); i++) {
            final var stack = tooltip.stacks().get(i);
            final var extra = new TextComponent("  \u2022 ").append(Utils.textComponent(Utils.getCompressedCount(stack.getCount())).withStyle(ChatFormatting.GOLD)).append(" x ");
            final var display = stack.getHoverName();
            final var name = Minecraft.getInstance().font.substrByWidth(display, MAX_STACK_TOOLTIP_LENGTH - Minecraft.getInstance().font.width(extra));
            contentsTooltips.add(extra.append(Utils.getComponent(name, display.getStyle())).getVisualOrderText());
        }
        if (tooltip.stacks().size() > MAX_SHOWN_STACKS)
            contentsTooltips.add(Translations.AND_MORE.translate(tooltip.stacks().size() - MAX_SHOWN_STACKS).getVisualOrderText());
        maxWidth = contentsTooltips.stream().mapToInt(Minecraft.getInstance().font::width).max().orElse(0);
    }

    @Override
    public int getHeight() {
        return Screen.hasShiftDown() ? contentsTooltips.size() * (Minecraft.getInstance().font.lineHeight + 2) : 20 + 2 + 4;
    }

    @Override
    public int getWidth(@NotNull Font pFont) {
        return Screen.hasShiftDown() ? maxWidth : getGridSize() * 18 + 2 + (tooltip.stacks().size() > MAX_TOOLTIP_LENGTH ? 20 : 0);
    }

    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        if (!Screen.hasShiftDown())
            return;
        for (int i = 0; i < contentsTooltips.size(); i++) {
            drawText(pFont, contentsTooltips.get(i), pX, pY + (i * (2 + pFont.lineHeight)), pMatrix4f, pBufferSource);
        }
    }

    private void drawText(Font pFont, FormattedCharSequence text, int x, int y, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {
        pFont.drawInBatch(text, x, y, -1, true, pMatrix4f, pBufferSource, false, 0, 15728880);
    }

    @Override
    public void renderImage(Font pFont, int pMouseX, int pMouseY, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        if (Screen.hasShiftDown())
            return;
        int i = getGridSize();
        int k = 0;

        for (int i1 = 0; i1 < i; ++i1) {
            int j1 = pMouseX + i1 * 18 + 1;
            int k1 = pMouseY + 1;
            this.renderSlot(j1, k1, k++, pFont, pPoseStack, pItemRenderer, pBlitOffset);
        }

        final var actualSize = Math.min(i, MAX_TOOLTIP_LENGTH);
        this.drawBorder(pMouseX, pMouseY, actualSize, 1, pPoseStack, pBlitOffset);
    }

    private void renderSlot(int pX, int pY, int pItemIndex, Font pFont, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        if (pItemIndex >= MAX_TOOLTIP_LENGTH) {
            pPoseStack.translate(0.0D, 0.0D, pBlitOffset + 200.0F);
            final var xOffset = 20;
            final var y = pY - 2;
            drawCenteredString(pPoseStack, pFont, Utils.textComponent(Utils.getCompressedCount(tooltip.stacks().size() - MAX_TOOLTIP_LENGTH)), pX + xOffset, y + 2, 0xffffff);
            drawCenteredString(pPoseStack, pFont, Translations.MORE.translate(), pX + xOffset, y + 5 + pFont.lineHeight, 0xf8f8ff);
        } else {
            final var itemstack = tooltip.stacks().get(pItemIndex);
            blit(pPoseStack, pX, pY, pBlitOffset, Texture.SLOT);
            pItemRenderer.renderAndDecorateItem(itemstack, pX + 1, pY + 1, pItemIndex);
            pItemRenderer.renderGuiItemDecorations(pFont, itemstack, pX + 1, pY + 1, Utils.getCompressedCount(itemstack.getCount()));
        }
    }

    public static void drawCenteredString(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        pFont.drawShadow(pPoseStack, formattedcharsequence, (float)(pX - pFont.width(formattedcharsequence) / 2), (float)pY, pColor);
    }

    private void drawBorder(int pX, int pY, int pSlotWidth, int pSlotHeight, PoseStack pPoseStack, int pBlitOffset) {
        blit(pPoseStack, pX, pY, pBlitOffset, Texture.BORDER_CORNER_TOP);
        blit(pPoseStack, pX + pSlotWidth * 18 + 1, pY, pBlitOffset, Texture.BORDER_CORNER_TOP);

        for (int i = 0; i < pSlotWidth; ++i) {
            blit(pPoseStack, pX + 1 + i * 18, pY, pBlitOffset, Texture.BORDER_HORIZONTAL_TOP);
            blit(pPoseStack, pX + 1 + i * 18, pY + pSlotHeight * 20, pBlitOffset, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for (int j = 0; j < pSlotHeight; ++j) {
            blit(pPoseStack, pX, pY + j * 20 + 1, pBlitOffset, Texture.BORDER_VERTICAL);
            blit(pPoseStack, pX + pSlotWidth * 18 + 1, pY + j * 20 + 1, pBlitOffset, Texture.BORDER_VERTICAL);
        }

        blit(pPoseStack, pX, pY + pSlotHeight * 20, pBlitOffset, Texture.BORDER_CORNER_BOTTOM);
        blit(pPoseStack, pX + pSlotWidth * 18 + 1, pY + pSlotHeight * 20, pBlitOffset, Texture.BORDER_CORNER_BOTTOM);
    }

    public static void blit(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, Texture pTexture) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ClientBundleTooltip.TEXTURE_LOCATION);
        GuiComponent.blit(pPoseStack, pX, pY, pBlitOffset, (float) pTexture.x, (float) pTexture.y, pTexture.w, pTexture.h, 128, 128);
    }

    public int getGridSize() {
        return tooltip.stacks().size() > MAX_TOOLTIP_LENGTH ? MAX_TOOLTIP_LENGTH + 1 : tooltip.stacks().size();
    }

    public enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        private Texture(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
        }
    }
}
