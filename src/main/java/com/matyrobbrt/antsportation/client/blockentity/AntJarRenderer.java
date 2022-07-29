package com.matyrobbrt.antsportation.client.blockentity;

import com.matyrobbrt.antsportation.block.AntJarBlock;
import com.matyrobbrt.antsportation.block.entity.AntJarBE;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AntJarRenderer implements BlockEntityRenderer<AntJarBE> {
    private AntQueenEntity queen;

    @Override
    public void render(AntJarBE antJarBE, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (antJarBE.getBlockState().getValue(AntJarBlock.ANTINSIDE)) {
            if (queen == null) queen = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), antJarBE.getLevel());
            Direction direction = antJarBE.getBlockState().getValue(AntJarBlock.FACING);
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(45 + (90 * direction.get3DDataValue())));
            poseStack.scale(0.3f, 0.3f, 0.3f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(queen, 0.0D, 0.0D, 0.0D, 0, v, poseStack, multiBufferSource, i);
        }
    }
}
