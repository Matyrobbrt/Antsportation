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
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SpellCheckingInspection")
public class AntWorkerModel<T extends AntWorkerEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "ant_worker.png"), "all");
    public static final ModelLayerLocation LAYER_LOCATION_BOX = new ModelLayerLocation(new ResourceLocation(Antsportation.MOD_ID, "box_model.png"), "bb_main");


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

    private final ModelPart antenna_east_r1;
    private final ModelPart antenna_west_r1;
    private final ModelPart leg_right_front_r1;
    private final ModelPart leg_right_back_r1;
    private final ModelPart leg_left_front_r1;
    private final ModelPart leg_left_middle_r1;
    private final ModelPart thorax_r1;

    private final ModelPart box;
    private final ModelPart eastFlap;
    private final ModelPart westFlap;
    private final ModelPart eastFlap_r1;
    private final ModelPart westFlap_r1;

    private AntWorkerEntity ant;



    public AntWorkerModel(ModelPart bone, ModelPart boxBone) {
        this.all = bone.getChild("all");
        this.head = all.getChild("head");
        this.antenna_east = head.getChild("antenna_east");
        this.antenna_west = head.getChild("antenna_west");
        this.antenna_east_r1 = antenna_east.getChild("antenna_east_r1");
        this.antenna_west_r1 = antenna_west.getChild("antenna_west_r1");
        this.body = all.getChild("body");
        this.legs_west = body.getChild("legs_west");
        this.legs_east = body.getChild("legs_east");
        this.leg_right_front = legs_east.getChild("leg_right_front");
        this.leg_right_middle = legs_east.getChild("leg_right_middle");
        this.leg_right_back = legs_east.getChild("leg_right_back");
        this.leg_left_front = legs_west.getChild("leg_left_front");
        this.leg_left_middle = legs_west.getChild("leg_left_middle");
        this.leg_left_back = legs_west.getChild("leg_left_back");
        this.leg_right_front_r1 = leg_right_front.getChild("leg_right_front_r1");
        this.leg_right_back_r1 = leg_right_back.getChild("leg_right_back_r1");
        this.leg_left_front_r1 = leg_left_front.getChild("leg_left_front_r1");
        this.leg_left_middle_r1 = leg_left_middle.getChild("leg_left_middle_r1");
        this.thorax = all.getChild("thorax");
        this.thorax_r1 = thorax.getChild("thorax_r1");
        this.box = boxBone.getChild("bb_main");
        this.eastFlap = box.getChild("Up_East");
        this.westFlap = box.getChild("Up_West");
        this.eastFlap_r1 = eastFlap.getChild("Up_East_r1");
        this.westFlap_r1 = westFlap.getChild("Up_West_r1");
    }
    public static LayerDefinition createBoxLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(7, 14).addBox(-2.0F, -3.9F, -2.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(14, 0).addBox(-2.0F, -3.9F, 1.9F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(1.9F, -3.9F, -1.9F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 1).addBox(-2.0F, -3.9F, -1.9F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.9F, -0.1F, -1.9F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Up_East = bb_main.addOrReplaceChild("Up_East", CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        PartDefinition Up_East_r1 = Up_East.addOrReplaceChild("Up_East_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-0.05F, 0.1F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.95F, 0.0F, 0.0F, 0.0F, 0.3927F));
        PartDefinition Up_West = bb_main.addOrReplaceChild("Up_West", CubeListBuilder.create(), PartPose.offset(0, 0, 0));
        PartDefinition Up_West_r1 = Up_West.addOrReplaceChild("Up_West_r1", CubeListBuilder.create().texOffs(5, 7).addBox(-0.05F, 0.1F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.95F, 0.0F, 0.0F, 0.0F, -0.3927F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.25F, -2.0F, 0.0F, 0.2151F, -0.0365F, -0.0066F));

        PartDefinition fang_west_r1 = head.addOrReplaceChild("fang_west_r1", CubeListBuilder.create().texOffs(0, 3).addBox(1.0F, 1.0F, -3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 3).addBox(-1.0F, 1.0F, -3.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -3.5782F, -2.2796F, -0.2182F, 0.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, -5.0F, -5.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition antenna_west = head.addOrReplaceChild("antenna_west", CubeListBuilder.create(), PartPose.offset(1.0F, -3.5782F, -2.2796F));

        PartDefinition antenna_west_r1 = antenna_west.addOrReplaceChild("antenna_west_r1", CubeListBuilder.create().texOffs(0, 0).addBox(2.76F, -2.5F, -0.7F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -1.9218F, -1.2204F, 0.6021F, 0.0F, 0.0F));

        PartDefinition antenna_east = head.addOrReplaceChild("antenna_east", CubeListBuilder.create(), PartPose.offset(1.0F, -3.5782F, -2.2796F));

        PartDefinition antenna_east_r1 = antenna_east.addOrReplaceChild("antenna_east_r1", CubeListBuilder.create().texOffs(2, 0).addBox(0.24F, -2.5F, -0.7F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -1.9218F, -1.2204F, 0.6021F, 0.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(12, 13).addBox(-0.5F, -2.5F, -1.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.25F, -2.0F, 0.0F));

        PartDefinition legs_east = body.addOrReplaceChild("legs_east", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 1.166F, 1.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition leg_right_back = legs_east.addOrReplaceChild("leg_right_back", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition leg_right_back_r1 = leg_right_back.addOrReplaceChild("leg_right_back_r1", CubeListBuilder.create().texOffs(14, 8).addBox(-1.5825F, -1.3152F, 0.2286F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition leg_right_middle = legs_east.addOrReplaceChild("leg_right_middle", CubeListBuilder.create().texOffs(13, 0).addBox(-1.5825F, -1.241F, -1.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition leg_right_front = legs_east.addOrReplaceChild("leg_right_front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_right_front_r1 = leg_right_front.addOrReplaceChild("leg_right_front_r1", CubeListBuilder.create().texOffs(15, 0).addBox(-1.5825F, -1.3152F, -1.2286F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition legs_west = body.addOrReplaceChild("legs_west", CubeListBuilder.create(), PartPose.offsetAndRotation(4.25F, 1.15F, 1.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition leg_left_back = legs_west.addOrReplaceChild("leg_left_back", CubeListBuilder.create().texOffs(12, 8).addBox(1.9175F, -2.491F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 0.0F, -0.5F));

        PartDefinition leg_left_middle = legs_west.addOrReplaceChild("leg_left_middle", CubeListBuilder.create(), PartPose.offset(-2.5F, 0.0F, -0.5F));

        PartDefinition leg_left_middle_r1 = leg_left_middle.addOrReplaceChild("leg_left_middle_r1", CubeListBuilder.create().texOffs(2, 8).addBox(-0.5825F, -2.3392F, 1.0455F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 1.5F, 0.6109F, 0.0F, 0.0F));

        PartDefinition leg_left_front = legs_west.addOrReplaceChild("leg_left_front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_left_front_r1 = leg_left_front.addOrReplaceChild("leg_left_front_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-0.5825F, -2.3392F, -2.0455F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition thorax = all.addOrReplaceChild("thorax", CubeListBuilder.create(), PartPose.offset(-1.25F, -2.0F, 0.0F));

        PartDefinition thorax_r1 = thorax.addOrReplaceChild("thorax_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.5F, 2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public void setupAnim(@NotNull T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        all.y = 35;
        ant = pEntity;
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.7f, 0.7f, 0.7f);
        all.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        if(!ant.getOffhandItem().isEmpty()) {
            box.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        }
        pPoseStack.popPose();
    }
}
