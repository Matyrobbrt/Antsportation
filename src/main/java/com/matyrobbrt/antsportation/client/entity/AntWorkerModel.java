package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

// Generated with BlockBench
public class AntWorkerModel<T extends AntWorkerEntity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "ant_worker.png"), "all");
    public static final ModelLayerLocation LAYER_LOCATION_BOX = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "box_model.png"), "bb_main");

    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart antenna1;
    private final ModelPart antenna2;
    private final ModelPart mouth1;
    private final ModelPart mouth2;
    private final ModelPart body;
    private final ModelPart thorax;
    private final ModelPart leg_left_front;
    private final ModelPart leg_left_middle;
    private final ModelPart leg_left_back;
    private final ModelPart leg_right_front;
    private final ModelPart leg_right_middle;
    private final ModelPart leg_right_back;

    private final ModelPart leg_right;
    private final ModelPart leg_left;
    private AntWorkerEntity ant;

    public AntWorkerModel(ModelPart bone) {
        this.all = bone.getChild("all");
        this.head = all.getChild("head");
        this.antenna1 = head.getChild("antenna1");
        this.antenna2 = head.getChild("antenna2");
        this.mouth1 = head.getChild("mouth1");
        this.mouth2 = head.getChild("mouth2");
        this.body = all.getChild("body");
        this.thorax = all.getChild("thorax");
        this.leg_left = body.getChild("legs_left");
        this.leg_right = all.getChild("legs_right");

        this.leg_left_front = leg_left.getChild("leg_left_front_r1");
        this.leg_left_middle = leg_left.getChild("leg_left_middle_r1");
        this.leg_left_back = leg_left.getChild("leg_left_back_r1");

        this.leg_right_front = leg_right.getChild("leg_right_front_r1");
        this.leg_right_middle = leg_right.getChild("leg_right_middle_r1");
        this.leg_right_back = leg_right.getChild("leg_right_back_r1");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 34.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(0.05F, -3.0F, -1.0F));

        head.addOrReplaceChild("mouth1", CubeListBuilder.create().texOffs(2, 3).addBox(-1.0F, 0.0218F, -4.9796F, 0.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("mouth2", CubeListBuilder.create().texOffs(0, 3).addBox(1.0F, 0.0218F, -4.9796F, 0.0F, 1.0F, 1.0F),
                PartPose.ZERO);

        head.addOrReplaceChild("antenna1", CubeListBuilder.create().texOffs(2, 0).addBox(-2.7F, -3.0F, -0.7F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.34F, -2.4179F, -3.385F, 0.6021F, 0.0F, 0.0F));
        head.addOrReplaceChild("antenna2", CubeListBuilder.create().texOffs(2, 0).addBox(0.0F, -3.0F, -0.7F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.34F, -2.4179F, -3.385F, 0.6021F, 0.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(12, 13).addBox(-1.1F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F),
                PartPose.offset(-0.35F, -2.5F, 1.0F));

        PartDefinition legs_left = body.addOrReplaceChild("legs_left", CubeListBuilder.create(), PartPose.offset(1.25F, 0.1F, 0.0F));

        legs_left.addOrReplaceChild("leg_left_back_r1", CubeListBuilder.create().texOffs(12, 8).addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.5175F, 0.0F, 1.0F, 0.6109F, 0.0F, -0.6109F));

        legs_left.addOrReplaceChild("leg_left_middle_r1", CubeListBuilder.create().texOffs(12, 8).addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.5175F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        legs_left.addOrReplaceChild("leg_left_front_r1", CubeListBuilder.create().texOffs(12, 8).addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(0.5175F, 0.0F, -1.0F, -0.6109F, 0.0F, -0.6109F));

        PartDefinition legs_right = all.addOrReplaceChild("legs_right", CubeListBuilder.create(), PartPose.offset(-1.1F, -2.4F, 1.0F));

        legs_right.addOrReplaceChild("leg_right_back_r1", CubeListBuilder.create().texOffs(12, 8).mirror().addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-0.2175F, 0.0F, 1.0F, 0.6109F, 0.0F, 0.6109F));

        legs_right.addOrReplaceChild("leg_right_middle_r1", CubeListBuilder.create().texOffs(12, 8).mirror().addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-0.2175F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        legs_right.addOrReplaceChild("leg_right_front_r1", CubeListBuilder.create().texOffs(12, 8).mirror().addBox(0.0F, 0.059F, -0.5F, 0.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-0.2175F, 0.0F, -1.0F, -0.6109F, 0.0F, 0.6109F));

        PartDefinition thorax = all.addOrReplaceChild("thorax", CubeListBuilder.create(), PartPose.offset(0.05F, -2.8878F, 3.0168F));

        thorax.addOrReplaceChild("thorax_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.6F, -0.2F, 4.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ant = pEntity;
        //Initial rot
        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
        head.xRot += Mth.sin(pAgeInTicks / 10f) / 30f;
        thorax.xRot = -Mth.sin(pAgeInTicks / 5f) / 20f;
        antenna1.xRot = Mth.sin(pAgeInTicks / 10f) / 10f + 0.6f;
        antenna2.xRot = Mth.sin(pAgeInTicks / 10f) / 10f + 0.6f;

        leg_right_back.xRot = 0;
        leg_right_back.xRot += -(Mth.cos(pLimbSwing * 0.6662F * 2.0F - 0.5F) * 0.5F) * pLimbSwingAmount * 2 + 0.5;
        leg_right_middle.xRot = 0;
        leg_right_middle.xRot += (Mth.cos(pLimbSwing * 0.6662f * 2.0F + 0.5F) * 0.5F) * pLimbSwingAmount * 2;
        leg_right_front.xRot = 0;
        leg_right_front.xRot += -(Mth.cos(pLimbSwing * 0.6662F * 2.0F - 0.5F) * 0.5F) * pLimbSwingAmount * 2 - 0.5f;

        leg_left_back.xRot = 0;
        leg_left_back.xRot += (Mth.cos(pLimbSwing * 0.6662F * 2.0F - 0.5F) * 0.5F) * pLimbSwingAmount * 2 + 0.5;
        leg_left_middle.xRot = 0;
        leg_left_middle.xRot += -(Mth.cos(pLimbSwing * 0.6662f * 2.0F + 0.5F) * 0.5F) * pLimbSwingAmount * 2;
        leg_left_front.xRot = 0;
        leg_left_front.xRot += (Mth.cos(pLimbSwing * 0.6662F * 2.0F - 0.5F) * 0.5F) * pLimbSwingAmount * 2 - 0.5f;

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.7f, 0.7f, 0.7f);
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        //        if (!ant.getOffhandItem().isEmpty()) {
        //            box.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        //        }
        pPoseStack.popPose();
    }
}
