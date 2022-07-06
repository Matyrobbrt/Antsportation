package com.matyrobbrt.antsportation.network;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
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
            <T extends Packet> void register(Class<T> clazz, @Nullable NetworkDirection direction, Function<FriendlyByteBuf, T> decoder) {
                CHANNEL.messageBuilder(clazz, id++, direction)
                        .encoder(Packet::encode)
                        .decoder(decoder)
                        .consumer((t, contextSupplier) -> {
                            final var ctx = contextSupplier.get();
                            ctx.enqueueWork(() -> t.handle(ctx));
                            return true;
                        })
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

    public static <MSG> void sendToAllTracking(MSG message, BlockEntity tile) {
        sendToAllTracking(message, tile.getLevel(), tile.getBlockPos());
    }

    public static <MSG> void sendToAllTracking(MSG message, Level world, BlockPos pos) {
        if (world instanceof ServerLevel level) {
            level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(message, p));
        } else {
            CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), message);
        }
    }
}
