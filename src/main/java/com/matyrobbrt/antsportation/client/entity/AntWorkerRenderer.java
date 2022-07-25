package com.matyrobbrt.antsportation.client.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.BoxModel;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
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
    public void render(AntWorkerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        VertexConsumer buffer = pBuffer.getBuffer(RENDER_TYPE);
        if (!pEntity.getOffhandItem().isEmpty()) {
            ItemStack boxitem = pEntity.getOffhandItem();
            if (boxitem.is(AntsportationTags.Items.BOXES)) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(0, 0.5, 0);
                pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(pEntityYaw+90));
                box.all.zRot = (float) Math.PI;
                box.all.y = -3.7f + -(float) Math.sin(0.1f * pEntity.tickCount) * 0.1f;
                box.all.x = 1.35f;
                box.all.z = 0f;
                box.all.yRot = (float) Math.PI / 2;
                box.flap1.y = -3;
                box.flap1.x = -2;
                box.flap2.x = 2;
                box.flap2.y = -3;
                box.flap1.zRot = (float) Math.cos(pEntity.tickCount / 10f + 15) * 0.2f + 400;
                box.flap2.zRot = -(float) Math.cos(pEntity.tickCount / 10f + 20) * 0.2f - 400;
                box.render(pMatrixStack, buffer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1, pEntity.tickCount, pEntity.tickCount);
                pMatrixStack.popPose();
                final var stackInBox = BoxItem.getStoredItems(boxitem).findFirst().map(BoxItem.ItemStackInstance::getStack).orElse(ItemStack.EMPTY);
                pMatrixStack.pushPose();
                pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(pEntityYaw+90));
                pMatrixStack.scale(0.3f, 0.3f, 0.3f);
                pMatrixStack.translate(0.4f, 1.2, 0);
                itemRenderer.renderStatic(null, stackInBox, ItemTransforms.TransformType.FIXED, false, pMatrixStack, pBuffer, pEntity.level, pPackedLight, OverlayTexture.NO_OVERLAY, 1);
                pMatrixStack.popPose();
            } else {
                pMatrixStack.pushPose();
                pMatrixStack.scale(0.3f, 0.3f, 0.3f);
                pMatrixStack.translate(0.4f, 1.1+-(float) Math.sin(0.1f * pEntity.tickCount) * 0.01f+0.05f, 0);
                itemRenderer.renderStatic(null, pEntity.getOffhandItem(), ItemTransforms.TransformType.FIXED, false, pMatrixStack, pBuffer, pEntity.level, pPackedLight, OverlayTexture.NO_OVERLAY, 1);
                pMatrixStack.popPose();
            }
        }

    }
}
