package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AntWorkerRenderer extends MobRenderer<AntWorkerEntity, AntWorkerModel<AntWorkerEntity>> {
    public static final ResourceLocation ANT_WORKER_LOCATION = new ResourceLocation(Antsportation.MOD_ID, "textures/entity/tempantqueen.png");

    public AntWorkerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AntWorkerModel<>(pContext.bakeLayer(AntWorkerModel.LAYER_LOCATION)), 0.1F);
    }

    @Override
    public ResourceLocation getTextureLocation(AntWorkerEntity pEntity) {
        return ANT_WORKER_LOCATION;
    }
}
