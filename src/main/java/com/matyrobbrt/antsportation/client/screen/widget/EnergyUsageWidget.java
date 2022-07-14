package com.matyrobbrt.antsportation.client.screen.widget;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

@ParametersAreNonnullByDefault
public record EnergyUsageWidget(int x, int y, BooleanSupplier enabled, IntSupplier storedAmount, IntSupplier maxAmount, Screen screen) implements Widget {
    public static final ResourceLocation TEXTURE = Antsportation.rl("textures/widget/energy_bar.png");

    public static final int WIDTH = 90;
    public static final int HEIGHT = 9;
    public static final int TEX_WIDTH = 90;
    public static final int TEX_HEIGHT = 18;

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float v) {
        if (!enabled.getAsBoolean())
            return;
        RenderSystem.setShaderTexture(0, TEXTURE);
        Screen.blit(poseStack, x, y, 0, 0, WIDTH, HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        Screen.blit(poseStack, x + 1, y + 1, 1, 10, getEnergyScaled(), 7, TEX_WIDTH, TEX_HEIGHT);
    }

    public void attemptTooltipRender(PoseStack poseStack, int pMouseX, int pMouseY) {
        if (enabled.getAsBoolean() && pMouseX >= this.x && pMouseY >= this.y && pMouseX < (this.x + WIDTH) && pMouseY < (this.y + HEIGHT)) {
            screen.renderTooltip(poseStack, Translations.STORED_ENERGY.translate(
                    Utils.textComponent(Utils.getCompressedCount(storedAmount.getAsInt()), s -> s.withColor(ChatFormatting.GOLD)),
                    Utils.textComponent(Utils.getCompressedCount(maxAmount.getAsInt()), s -> s.withColor(ChatFormatting.AQUA))
            ), pMouseX, pMouseY);
        }
    }

    public int getEnergyScaled() {
        return storedAmount.getAsInt() != 0 && maxAmount.getAsInt() != 0
                ? storedAmount.getAsInt() * 88 / maxAmount.getAsInt()
                : 0;
    }
}
