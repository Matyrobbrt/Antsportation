package com.matyrobbrt.antsportation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class RequestUpdatePacket implements Packet {
    public static final RequestUpdatePacket INSTANCE = new RequestUpdatePacket();

    private RequestUpdatePacket() {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (context.getSender() != null) {
            context.getSender().containerMenu.broadcastChanges();
        }
    }

    public static RequestUpdatePacket decode(FriendlyByteBuf buf) {
        return INSTANCE;
    }
}
