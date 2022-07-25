package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
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
public class AntQueenModel<T extends AntQueenEntity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "ant_queen.png"), "all");

    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart antenna1;
    private final ModelPart antenna2;
    private final ModelPart body;
    private final ModelPart thorax_all;
    private final ModelPart leg_left_front;
    private final ModelPart leg_left_middle;
    private final ModelPart leg_left_back;
    private final ModelPart leg_right_front;
    private final ModelPart leg_right_middle;
    private final ModelPart leg_right_back;

    private final ModelPart leg_right;
    private final ModelPart leg_left;

    public AntQueenModel(ModelPart bone) {
        this.all = bone.getChild("all");
        this.head = all.getChild("head");

        this.antenna1 = head.getChild("antenna1");
        this.antenna2 = head.getChild("antenna2");
        this.body = all.getChild("body_all");

        this.thorax_all = all.getChild("thorax_all");

        this.leg_left = body.getChild("legs_left");
        this.leg_right = body.getChild("legs_right");

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

        PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, -6.0F, -2.0F));
        head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(27, 5).addBox(-2.0104F, -2.9895F, -3.4098F, 4.0F, 1.0F, 3.0F), PartPose.ZERO);
        head.addOrReplaceChild("mouth1", CubeListBuilder.create().texOffs(0, 1).addBox(1.0F, 1.0F, -5.0F, 0.0F, 1.0F, 1.0F), PartPose.ZERO);
        head.addOrReplaceChild("mouth2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.0F, -5.0F, 0.0F, 1.0F, 1.0F), PartPose.ZERO);

        head.addOrReplaceChild("antenna1", CubeListBuilder.create().texOffs(2, 25).addBox(0.0F, -3.75F, -1.0F, 0.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(1.75F, -2.0F, -3.5F, 0.8727F, 0.0F, 0.0F));
        head.addOrReplaceChild("antenna2", CubeListBuilder.create().texOffs(2, 25).addBox(-3.5F, -3.75F, -1.0F, 0.0F, 4.0F, 1.0F),
                PartPose.offsetAndRotation(1.75F, -2.0F, -3.5F, 0.8727F, 0.0F, 0.0F));

        PartDefinition body_all = all.addOrReplaceChild("body_all", CubeListBuilder.create().texOffs(15, 3).addBox(-0.25F, -4.75F, -1.0F, 3.0F, 2.0F, 6.0F),
                PartPose.offset(-1.25F, -1.5F, -2.0F));

        PartDefinition legs_left = body_all.addOrReplaceChild("legs_left", CubeListBuilder.create(), PartPose.offset(1.25F, -1.0F, 2.5F));
        legs_left.addOrReplaceChild("leg_left_back_r1", CubeListBuilder.create().texOffs(18, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, -2.5F, 1.0F, 0.3927F, 0.0F, -0.3491F));
        legs_left.addOrReplaceChild("leg_left_middle_r1", CubeListBuilder.create().texOffs(18, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, -2.5F, 0.0F, 0.0F, 0.0F, -0.3491F));
        legs_left.addOrReplaceChild("leg_left_front_r1", CubeListBuilder.create().texOffs(18, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, -2.5F, -1.0F, -0.3927F, 0.0F, -0.3491F));

        PartDefinition legs_right = body_all.addOrReplaceChild("legs_right", CubeListBuilder.create(), PartPose.offset(1.25F, -1.0F, 2.5F));
        legs_right.addOrReplaceChild("leg_right_back_r1", CubeListBuilder.create().texOffs(18, 21).mirror().addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, -2.5F, 1.0F, 0.3927F, 0.0F, 0.3491F));
        legs_right.addOrReplaceChild("leg_right_middle_r1", CubeListBuilder.create().texOffs(18, 21).mirror().addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, -2.5F, 0.0F, 0.0F, 0.0F, 0.3491F));
        legs_right.addOrReplaceChild("leg_right_front_r1", CubeListBuilder.create().texOffs(18, 21).mirror().addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(-1.5F, -2.5F, -1.0F, -0.3927F, 0.0F, 0.3491F));

        PartDefinition thorax_all = all.addOrReplaceChild("thorax_all", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, 2.25F, 0.5236F, 0.0F, 0.0F));
        thorax_all.addOrReplaceChild("thorax_1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.5F, -0.0037F, 6.0F, 5.0F, 4.0F), PartPose.ZERO);
        thorax_all.addOrReplaceChild("thorax_2", CubeListBuilder.create().texOffs(15, 14).addBox(-3.0F, -2.5F, 4.0F, 6.0F, 5.0F, 3.0F), PartPose.ZERO);
        thorax_all.addOrReplaceChild("thorax_3", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -2.5F, 7.0F, 6.0F, 5.0F, 3.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        all.y = 35;
        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);

        head.xRot += Mth.sin(pAgeInTicks / 10f) / 30f - 0.02f;
        thorax_all.xRot = -Mth.sin(pAgeInTicks / 3.5f) / 20f + 0.5f;
        var f1 = -Mth.sin(pAgeInTicks / 10f) / 10f + 0.8f;
        antenna1.xRot = f1;
        antenna2.xRot = f1;

        var f2 = (Mth.cos(pLimbSwing * 0.6662F * 2.0F - 0.5F) * 0.5F) * pLimbSwingAmount;
        var f3 = (Mth.cos(pLimbSwing * 0.6662f * 2.0F + 0.5F) * 0.5F) * pLimbSwingAmount;
        leg_right_back.xRot = 0;
        leg_right_back.xRot += -f2 + 0.5;
        leg_right_middle.xRot = 0;
        leg_right_middle.xRot += f3;
        leg_right_front.xRot = 0;
        leg_right_front.xRot += -f2 - 0.5f;

        leg_left_back.xRot = 0;
        leg_left_back.xRot += f2 + 0.5;
        leg_left_middle.xRot = 0;
        leg_left_middle.xRot += -f3;
        leg_left_front.xRot = 0;
        leg_left_front.xRot += f2 - 0.5f;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.7f, 0.7f, 0.7f);
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        pPoseStack.popPose();
    }
}
