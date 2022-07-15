package com.matyrobbrt.antsportation.client.screen.widget;

import com.matyrobbrt.antsportation.Antsportation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

public record ProgressWidget(int x, int y, IntSupplier progress, boolean flipped) implements Widget {
    public static final ResourceLocation TEXTURE = Antsportation.rl("textures/widget/arrows.png");
    public static final int MAX_PROGRESS = 24;
    public static final int HEIGHT = 17;
    public static final int TEX_HEIGHT = 48;
    public static final int TEX_WIDTH = 48;

    public static void bindTexture() {
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    public static void renderFlipped(int progress, int x, int y, PoseStack pPoseStack) {
        Screen.blit(pPoseStack, x, y, 24, 31, MAX_PROGRESS, HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        for (int i = 0; i < progress; i++) {
            final int relative = (MAX_PROGRESS - i);
            Screen.blit(pPoseStack, x + relative, y, 48 - i, 0, 1, HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        }
    }
    public static void renderNormal(int progress, int x, int y, PoseStack pPoseStack) {
        final int remaining = MAX_PROGRESS - progress;
        if (remaining > 0)
            Screen.blit(pPoseStack, x + progress, y, progress, 31, remaining, HEIGHT, TEX_WIDTH, TEX_HEIGHT);
        if (progress > 0)
            Screen.blit(pPoseStack, x, y, 0, 0, progress, HEIGHT, TEX_WIDTH, TEX_HEIGHT);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        bindTexture();
        if (flipped) {
            renderFlipped(progress.getAsInt(), x, y, poseStack);
        } else {
            renderNormal(progress.getAsInt(), x, y, poseStack);
        }
    }
}
