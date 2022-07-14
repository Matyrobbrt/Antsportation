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

@SuppressWarnings("SpellCheckingInspection")
public class AntQueenModel<T extends AntQueenEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "ant_queen.png"), "all");
    private final ModelPart all;
    private final ModelPart head;
    private final ModelPart antenna_east;
    private final ModelPart antenna_west;
    private final ModelPart body;
    private final ModelPart legs_east;
    private final ModelPart legs_west;
    private final ModelPart leg_right_front;
    private final ModelPart leg_right_middle;
    private final ModelPart leg_right_back;
    private final ModelPart leg_left_front;
    private final ModelPart leg_left_middle;
    private final ModelPart leg_left_back;
    private final ModelPart thorax;



    public AntQueenModel(ModelPart bone) {
        this.all = bone.getChild("all");
        this.head = all.getChild("head");
        this.antenna_east = head.getChild("antenna_east");
        this.antenna_west = head.getChild("antenna_west");
        this.body = all.getChild("body");
        this.legs_west = body.getChild("legs_west");
        this.legs_east = body.getChild("legs_east");
        this.leg_right_front = legs_east.getChild("leg_right_front");
        this.leg_right_middle = legs_east.getChild("leg_right_middle");
        this.leg_right_back = legs_east.getChild("leg_right_back");
        this.leg_left_front = legs_west.getChild("leg_left_front");
        this.leg_left_middle = legs_west.getChild("leg_left_middle");
        this.leg_left_back = legs_west.getChild("leg_left_back");
        this.thorax = all.getChild("thorax");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -2.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-2.4558F, -1.0799F, 0.0079F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 1).addBox(-0.5735F, 1.9337F, -0.931F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.6765F, 1.9337F, -0.931F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.2396F, -7.2395F, -3.9098F));

        PartDefinition antenna_east = head.addOrReplaceChild("antenna_east", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition antenna_east_r1 = antenna_east.addOrReplaceChild("antenna_east_r1", CubeListBuilder.create().texOffs(2, 25).addBox(-0.01F, -4.2265F, -0.5692F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.8235F, -0.2381F, 0.5985F, 0.6021F, 0.0F, 0.0F));

        PartDefinition antenna_west = head.addOrReplaceChild("antenna_west", CubeListBuilder.create(), PartPose.offset(-1.8235F, -0.9881F, 0.8485F));

        PartDefinition antenna_west_r1 = antenna_west.addOrReplaceChild("antenna_west_r1", CubeListBuilder.create().texOffs(0, 25).addBox(0.01F, -4.2265F, -0.5692F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.75F, 0.75F, -0.25F, 0.6021F, 0.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(15, 3).addBox(-0.25F, -4.5F, -1.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.25F, -1.5F, 0.0F));

        PartDefinition legs_east = body.addOrReplaceChild("legs_east", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, -0.834F, 2.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition leg_right_front = legs_east.addOrReplaceChild("leg_right_front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_right_front_r1 = leg_right_front.addOrReplaceChild("leg_right_front_r1", CubeListBuilder.create().texOffs(24, 21).addBox(-0.0325F, -0.0291F, -0.4501F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.55F, -1.5F, -0.9F, -0.6109F, 0.0F, 0.0F));

        PartDefinition leg_right_middle = legs_east.addOrReplaceChild("leg_right_middle", CubeListBuilder.create().texOffs(20, 21).addBox(-2.4985F, 2.7308F, 3.3408F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.916F, -3.9719F, -3.8408F));

        PartDefinition leg_right_back = legs_east.addOrReplaceChild("leg_right_back", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_right_back_r1 = leg_right_back.addOrReplaceChild("leg_right_back_r1", CubeListBuilder.create().texOffs(22, 21).addBox(0.0175F, -0.0291F, -0.5499F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -1.5F, 0.9F, 0.6109F, 0.0F, 0.0F));

        PartDefinition legs_west = body.addOrReplaceChild("legs_west", CubeListBuilder.create(), PartPose.offsetAndRotation(4.25F, -0.85F, 2.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition leg_left_front = legs_west.addOrReplaceChild("leg_left_front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_left_front_r1 = leg_left_front.addOrReplaceChild("leg_left_front_r1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0175F, -0.7255F, -0.4273F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6F, -2.25F, -1.4F, -0.6109F, 0.0F, 0.0F));

        PartDefinition leg_left_middle = legs_west.addOrReplaceChild("leg_left_middle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition leg_left_middle_r1 = leg_left_middle.addOrReplaceChild("leg_left_middle_r1", CubeListBuilder.create().texOffs(2, 16).addBox(0.0175F, -0.6968F, -0.5317F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6F, -2.25F, 0.35F, 0.6109F, 0.0F, 0.0F));

        PartDefinition leg_left_back = legs_west.addOrReplaceChild("leg_left_back", CubeListBuilder.create().texOffs(18, 21).addBox(1.9175F, -2.491F, 0.0F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.0F, -0.5F));

        PartDefinition thorax = all.addOrReplaceChild("thorax", CubeListBuilder.create(), PartPose.offset(-1.25F, -1.5F, 0.0F));

        PartDefinition thorax_r1 = thorax.addOrReplaceChild("thorax_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -6.5F, 11.5F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(15, 14).addBox(-1.5F, -6.5F, 8.5F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition thorax_r2 = thorax.addOrReplaceChild("thorax_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.75F, -3.4992F, -0.116F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.25F, -4.25F, 3.5F, 0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.leg_left_front.yRot = ((float) Math.toRadians(pLimbSwingAmount * 360));

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.7f, 0.7f, 0.7f);
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        pPoseStack.popPose();
    }
}
