package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class BlockEntityProperty<T> {
    private static final CopyOnWriteArrayList<BlockEntityProperty<?>> REGISTRY = new CopyOnWriteArrayList<>();

    public static final BlockEntityProperty<Integer> ENERGY_USAGE = intProperty(Antsportation.rl("energy_usage"));

    private final int id;
    private final ResourceLocation name;
    private final BiConsumer<FriendlyByteBuf, T> encoder;
    private final Function<FriendlyByteBuf, T> decoder;
    private final Function<T, Tag> nbtEncoder;
    private final Function<Tag, T> nbtDecoder;

    public BlockEntityProperty(ResourceLocation name, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, Function<T, Tag> nbtEncoder, Function<Tag, T> nbtDecoder) {
        this.encoder = encoder;
        this.name = name;
        this.decoder = decoder;
        this.nbtEncoder = nbtEncoder;
        this.nbtDecoder = nbtDecoder;
        this.id = REGISTRY.size();
        REGISTRY.add(this);
    }

    public static BlockEntityProperty<Integer> intProperty(ResourceLocation name) {
        return new BlockEntityProperty<>(name, FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt, IntTag::valueOf, e -> ((IntTag) e).getAsInt());
    }
    public static <T extends Enum<T>> BlockEntityProperty<T> enumProperty(ResourceLocation name, Class<T> clazz) {
        return new BlockEntityProperty<>(name, FriendlyByteBuf::writeEnum, buf -> buf.readEnum(clazz), e -> IntTag.valueOf(e.ordinal()), e -> clazz.getEnumConstants()[((IntTag) e).getAsInt()]);
    }

    public int getId() {
        return id;
    }

    public ResourceLocation name() {
        return name;
    }

    public BiConsumer<FriendlyByteBuf, T> encoder() {
        return encoder;
    }
    @SuppressWarnings("unchecked")
    public void encodeUnsafe(Object object, FriendlyByteBuf buf) {
        encoder().accept(buf, (T) object);
    }

    public Function<FriendlyByteBuf, T> decoder() {
        return decoder;
    }

    public Function<T, Tag> nbtEncoder() {
        return nbtEncoder;
    }

    public Function<Tag, T> nbtDecoder() {
        return nbtDecoder;
    }

    public static BlockEntityProperty<?> getProperty(int id) {
        return REGISTRY.get(id);
    }
}
