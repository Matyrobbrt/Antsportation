package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AntWorkerRenderer extends MobRenderer<AntWorkerEntity, AntWorkerModel<AntWorkerEntity>> {

    public static final ResourceLocation ANT_WORKER_LOCATION = new ResourceLocation(Antsportation.MOD_ID, "textures/entity/ant_worker.png");

    public AntWorkerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AntWorkerModel<>(pContext.bakeLayer(AntWorkerModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(AntWorkerEntity pEntity) {
        return ANT_WORKER_LOCATION;
    }
}
