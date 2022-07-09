package com.matyrobbrt.antsportation.client.blockentity;

import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MarkerRenderer implements BlockEntityRenderer<MarkerBE> {

    public MarkerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MarkerBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        var sugarAmount = pBlockEntity.getSugarAmount();
        var color = pBlockEntity.getColor();
        var textureDiffuseColors = color.getTextureDiffuseColors();
        float smallSugar = 0.25f;
        if (sugarAmount > 0) {
            smallSugar = sugarAmount / 10f;
        }
        pPoseStack.pushPose();
        TextureAtlasSprite sugarTexture = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("item/sugar"));
        pPoseStack.translate(0.5f, 0.3f, 0.5f);
        pPoseStack.translate(-0.5f * smallSugar, 0, -0.5f * smallSugar);
        pPoseStack.scale(smallSugar, 0, smallSugar);
        renderTexture(sugarTexture, textureDiffuseColors[0], textureDiffuseColors[1], textureDiffuseColors[2], 1, pPackedLight, RenderType.cutout(), pPoseStack, pBufferSource);
        pPoseStack.popPose();
    }

    private void renderTexture(TextureAtlasSprite texture, float r, float g, float b, float a, int packedLight, RenderType renderType, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer builder = buffer.getBuffer(renderType);
        add(builder, poseStack, 0f, 0f, 1f, r, g, b, a, packedLight, texture.getU0(), texture.getV0());
        add(builder, poseStack, 1f, 0f, 1f, r, g, b, a, packedLight, texture.getU1(), texture.getV0());
        add(builder, poseStack, 1f, 0f, 0f, r, g, b, a, packedLight, texture.getU1(), texture.getV1());
        add(builder, poseStack, 0f, 0f, 0f, r, g, b, a, packedLight, texture.getU0(), texture.getV1());
    }

    public static void register() {
        BlockEntityRenderers.register(AntsportationBlocks.MARKER_BE.get(), MarkerRenderer::new);
    }

    private static void add(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float a, int packedLight, float u, float v) {
        renderer.vertex(stack.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).uv2(packedLight).normal(1, 1, 1).endVertex();
    }
}
