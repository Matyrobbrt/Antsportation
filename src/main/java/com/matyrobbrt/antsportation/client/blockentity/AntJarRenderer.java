package com.matyrobbrt.antsportation.client.blockentity;

import com.matyrobbrt.antsportation.block.AntJarBlock;
import com.matyrobbrt.antsportation.block.entity.AntJarBE;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AntJarRenderer implements BlockEntityRenderer<AntJarBE> {
    private AntQueenEntity queen;

    @Override
    public void render(AntJarBE antJarBE, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (antJarBE.getBlockState().getValue(AntJarBlock.ANTINSIDE)) {
            if (queen == null) queen = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), antJarBE.getLevel());
            poseStack.translate(0.53, 0, 0.53);
            poseStack.mulPose(new Quaternion(0, 45, 0, true));
            poseStack.scale(0.4f, 0.4f, 0.4f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(queen, 0.0D, 0.0D, 0.0D, 0, v, poseStack, multiBufferSource, i);
        }
    }
}
