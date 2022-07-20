package com.matyrobbrt.antsportation.client.blockentity;

import com.matyrobbrt.antsportation.block.AntJarBlock;
import com.matyrobbrt.antsportation.block.entity.AntJarBE;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class AntJarRenderer implements BlockEntityRenderer<AntJarBE> {
    public AntJarRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(AntJarBE antJarBE, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (antJarBE.getBlockState().getValue(AntJarBlock.ANTINSIDE)){
            poseStack.translate(0.53, 0, 0.53);
            poseStack.mulPose(new Quaternion(0, 45, 0, true));
            poseStack.scale(0.4f, 0.4f, 0.4f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(AntsportationEntities.ANT_QUEEN.get().create(antJarBE.getLevel()), 0.0D, 0.0D, 0.0D, 0, v, poseStack, multiBufferSource, i);
        }
    }
    public static void register() {
        BlockEntityRenderers.register(AntsportationBlocks.ANT_JAR_BE.get(), AntJarRenderer::new);
    }
}
