package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AntSoldierRenderer extends MobRenderer<AntSoldierEntity, AntSoldierModel<AntSoldierEntity>> {
    public static final ResourceLocation ANT_SOLDIER_LOCATION = new ResourceLocation(Antsportation.MOD_ID, "textures/entity/tempantqueen.png");

    public AntSoldierRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AntSoldierModel<>(pContext.bakeLayer(AntSoldierModel.LAYER_LOCATION)), 0.1F);
    }

    @Override
    public ResourceLocation getTextureLocation(AntSoldierEntity pEntity) {
        return ANT_SOLDIER_LOCATION;
    }
}
