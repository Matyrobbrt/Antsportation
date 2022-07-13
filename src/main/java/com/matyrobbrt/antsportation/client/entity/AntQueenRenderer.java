package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AntQueenRenderer extends MobRenderer<AntQueenEntity, AntQueenModel<AntQueenEntity>> {
    public static final ResourceLocation ANT_QUEEN_LOCATION = new ResourceLocation(Antsportation.MOD_ID, "textures/entity/ant_queen.png");

    public AntQueenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AntQueenModel<>(pContext.bakeLayer(AntQueenModel.LAYER_LOCATION)), 0.1F);
    }

    @Override
    public ResourceLocation getTextureLocation(AntQueenEntity pEntity) {
        return ANT_QUEEN_LOCATION;
    }
}
