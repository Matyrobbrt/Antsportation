package com.matyrobbrt.antsportation.network;

import com.matyrobbrt.antsportation.block.entity.BoxerBE;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record UpdateBoxerPacket(BlockPos pos, int type, int newValue) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeVarInt(type);
        buf.writeVarInt(newValue);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (context.getSender() == null)
            return;
        if (context.getSender().level.getBlockEntity(pos) instanceof BoxerBE boxerBE) {
            if (type == 0) {
                boxerBE.releasePercent = newValue;
            } else {
                boxerBE.redstoneControl = RedstoneControl.values()[newValue];
            }
            boxerBE.setChanged();
        }
    }

    public static UpdateBoxerPacket decode(FriendlyByteBuf buf) {
        return new UpdateBoxerPacket(buf.readBlockPos(), buf.readVarInt(), buf.readVarInt());
    }
}
