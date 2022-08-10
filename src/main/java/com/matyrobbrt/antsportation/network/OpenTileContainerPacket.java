package com.matyrobbrt.antsportation.network;

import com.matyrobbrt.antsportation.block.entity.HasMultipleMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public record OpenTileContainerPacket(BlockPos pos, @Nullable Byte index) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        if (index != null) {
            buf.writeBoolean(true);
            buf.writeByte(index);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (context.getSender() == null)
            return;
        if (index == null) {
            if (context.getSender().getLevel().getBlockEntity(pos) instanceof MenuProvider menuProvider)
                NetworkHooks.openScreen(context.getSender(), menuProvider, pos);
        } else {
            if (context.getSender().getLevel().getBlockEntity(pos) instanceof HasMultipleMenus multiple) {
                final var menu = multiple.getMenu(index);
                if (menu != null)
                    NetworkHooks.openScreen(context.getSender(), menu, pos);
            }
        }
    }
    public static OpenTileContainerPacket decode(FriendlyByteBuf friendlyByteBuf) {
        final var pos = friendlyByteBuf.readBlockPos();
        final var isntNull = friendlyByteBuf.readBoolean();
        final Byte index;
        if (isntNull) {
            index = friendlyByteBuf.readByte();
        } else {
            index = null;
        }
        return new OpenTileContainerPacket(pos, index);
    }
}
