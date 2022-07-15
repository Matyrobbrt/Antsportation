package com.matyrobbrt.antsportation.compat.top;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.widget.ProgressWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.apiimpl.client.ElementItemStackRender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record StackWithProgressElement(ItemStack stack, int scaledProgress, Direction direction) implements IElement {
    public static final ResourceLocation ID = Antsportation.rl("stack_with_progress");

    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    @Override
    public void render(PoseStack poseStack, int x, int y) {
        if (direction == Direction.LEFT_TO_RIGHT) {
            ElementItemStackRender.render(stack, AntsportationTOPProvider.defaultItemStyle, poseStack, x, y);
            ProgressWidget.bindTexture();
            ProgressWidget.renderNormal(scaledProgress(), x + 5 + AntsportationTOPProvider.defaultItemStyle.getWidth(), y, poseStack);
        } else {
            ProgressWidget.bindTexture();
            ProgressWidget.renderFlipped(scaledProgress(), x, y, poseStack);
            ElementItemStackRender.render(stack, AntsportationTOPProvider.defaultItemStyle, poseStack, x + 5 + ProgressWidget.MAX_PROGRESS, y + 2);
        }
    }

    @Override
    public int getWidth() {
        return ProgressWidget.MAX_PROGRESS + 5 + AntsportationTOPProvider.defaultItemStyle.getWidth();
    }

    @Override
    public int getHeight() {
        return ProgressWidget.HEIGHT;
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeItemStack(stack, true);
        friendlyByteBuf.writeShort(scaledProgress());
        friendlyByteBuf.writeEnum(direction());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static final class Factory implements IElementFactory {

        @Override
        public IElement createElement(FriendlyByteBuf friendlyByteBuf) {
            return new StackWithProgressElement(friendlyByteBuf.readItem(),
                    friendlyByteBuf.readShort(), friendlyByteBuf.readEnum(Direction.class));
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
