package com.matyrobbrt.antsportation.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoxModel extends Model {

    private final ModelPart all;
    private final ModelPart flap1;
    private final ModelPart flap2;

    public BoxModel(ModelPart bone) {
        super(RenderType::entitySolid);
        this.all = bone.getChild("all");
        this.flap1 = all.getChild("flap1");
        this.flap2 = all.getChild("flap2");
    }

    public static LayerDefinition createBoxLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create().texOffs(7, 14).addBox(-2.0F, -3.9F, -2.0F, 4.0F, 3.0F, 0.0F)
                .texOffs(14, 0).addBox(-2.0F, -3.9F, 1.9F, 4.0F, 3.0F, 0.0F)
                .texOffs(0, 11).addBox(1.9F, -3.9F, -1.9F, 0.0F, 3.0F, 3.0F)
                .texOffs(10, 1).addBox(-2.0F, -3.9F, -1.9F, 0.0F, 3.0F, 3.0F)
                .texOffs(0, 0).addBox(-1.9F, -0.1F, -1.9F, 3.0F, 0.0F, 3.0F), PartPose.offset(0.0F, 24.0F, 0.0F));

        all.addOrReplaceChild("flap1", CubeListBuilder.create().texOffs(0, 4).addBox(-0.05F, 0.1F, -2.0F, 0.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, -3.95F, 0.0F, 0.0F, 0.0F, 0.3927F));
        all.addOrReplaceChild("flap2", CubeListBuilder.create().texOffs(5, 7).addBox(-0.05F, 0.1F, -2.0F, 0.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, -3.95F, 0.0F, 0.0F, 0.0F, -0.3927F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public void render(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float r, float g, float b, float a, float flap1Anim, float flap2Anim) {
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, r, g, b, a);
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha, 0, 0);
    }
}
