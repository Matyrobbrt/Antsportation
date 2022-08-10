package com.matyrobbrt.antsportation.network;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.function.Function;

public class AntsportationNetwork {
    public static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(Antsportation.rl("network"))
            .networkProtocolVersion(() -> VERSION)
            .clientAcceptedVersions(e -> e.equals(VERSION))
            .serverAcceptedVersions(e -> e.equals(VERSION))
            .simpleChannel();
    public static void register() {
        class Registrar {
            int id;
            @SuppressWarnings("SameParameterValue")
            <T extends Packet> void register(Class<T> clazz, @Nullable NetworkDirection direction, Function<FriendlyByteBuf, T> decoder) {
                CHANNEL.messageBuilder(clazz, id++, direction)
                        .encoder(Packet::encode)
                        .decoder(decoder)
                        .consumerMainThread((t, contextSupplier) -> t.handle(contextSupplier.get()))
                        .add();
            }
        }

        final var registrar = new Registrar();
        registrar.register(OpenTileContainerPacket.class, null, OpenTileContainerPacket::decode);
        registrar.register(UpdateBoxerPacket.class, null, UpdateBoxerPacket::decode);
        registrar.register(RequestUpdatePacket.class, null, RequestUpdatePacket::decode);
    }

    public static  <MSG> void sendTo(MSG message, ServerPlayer player) {
        if (!(player instanceof FakePlayer)) {
            CHANNEL.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
