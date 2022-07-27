package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.BoxModel;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class AntWorkerRenderer extends MobRenderer<AntWorkerEntity, AntWorkerModel<AntWorkerEntity>> {

    public static final ResourceLocation ANT_WORKER_LOCATION = new ResourceLocation(Antsportation.MOD_ID, "textures/entity/ant_worker.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(Antsportation.rl("textures/entity/box_model.png"));

    private final BoxModel box;
    private final ItemRenderer itemRenderer;

    public AntWorkerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AntWorkerModel<>(pContext.bakeLayer(AntWorkerModel.LAYER_LOCATION)), 0.3F);
        this.itemRenderer = pContext.getItemRenderer();
        box = new BoxModel(pContext.bakeLayer(AntWorkerModel.LAYER_LOCATION_BOX));
    }

    @Override
    public ResourceLocation getTextureLocation(AntWorkerEntity pEntity) {
        return ANT_WORKER_LOCATION;
    }

    @Override
    public void render(AntWorkerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        var buffer = pBuffer.getBuffer(RENDER_TYPE);
        if (!pEntity.getOffhandItem().isEmpty()) {
            var boxitem = pEntity.getOffhandItem();
            var itemStack = pEntity.getOffhandItem();
            var f1 = 0.0275f;
            if (boxitem.is(AntsportationTags.Items.BOXES)) {
                pPoseStack.pushPose();
                pPoseStack.translate(0, 0.51, 0);
                pPoseStack.mulPose(Vector3f.YN.rotationDegrees(pEntityYaw + 90));
                box.all.xRot = Mth.sin(pEntity.tickCount / 10f) / 30f;
                box.all.zRot = (float) Math.PI;
                box.all.y = -3.7f + -(float) Math.sin(0.1f * pEntity.tickCount) * 0.05f;
                box.all.x = 1.35f;
                box.all.z = 0f;
                box.all.yRot = (float) Math.PI / 2;
                box.flap1.y = -3;
                box.flap1.x = -2;
                box.flap2.x = 2;
                box.flap2.y = -3;
                box.flap1.zRot = (float) Math.cos(pEntity.tickCount / 10f + 15) * 0.2f + 400;
                box.flap2.zRot = -(float) Math.cos(pEntity.tickCount / 10f + 20) * 0.2f - 400;
                box.render(pPoseStack, buffer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                pPoseStack.popPose();
                itemStack = BoxItem.getStoredItems(boxitem).findFirst().map(BoxItem.ItemStackInstance::getStack).orElse(ItemStack.EMPTY);
                f1 = 0;
            } else {
                if (!(itemStack.getItem() instanceof BlockItem)) {
                    f1 = 0f;
                }
            }
            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.YN.rotationDegrees(pEntityYaw));
            pPoseStack.translate(0.0f, f1 + 0.29f - Math.sin(0.1f * pEntity.tickCount) * 0.005f, 0f);
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            pPoseStack.mulPose(Vector3f.XP.rotation(Mth.sin(pEntity.tickCount / 10f) / 80f));
            pPoseStack.translate(0.0f, (f1 + 0.29f - Math.sin(0.1f * pEntity.tickCount) * 0.005f) * 0.275f, 0f);
            pPoseStack.scale(0.275f, 0.275f, 0.275f);
            itemRenderer.renderStatic(null, itemStack, ItemTransforms.TransformType.GROUND, false, pPoseStack, pBuffer, pEntity.level, pPackedLight, OverlayTexture.NO_OVERLAY, 1);
            pPoseStack.popPose();

        }

    }
}
