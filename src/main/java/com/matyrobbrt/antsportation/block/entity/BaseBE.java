package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.network.AntsportationNetwork;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseBE extends BlockEntity {

    private final Int2ObjectMap<PropertyInstance<?>> properties = new Int2ObjectOpenHashMap<>();

    public BaseBE(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    protected <T> PropertyInstance<T> grabProperty(BlockEntityProperty<T> type, T initialValue) {
        return grabProperty(type, initialValue, false, true);
    }

    protected <T> PropertyInstance<T> grabProperty(BlockEntityProperty<T> type, T initialValue, boolean synced, boolean saved) {
        final var insn = new PropertyInstance<>(type, synced, saved);
        properties.put(type.getId(), insn);
        insn.value = initialValue;
        if (synced)
            insn.addListener(this::sendCheckedUpdatePacket);
        insn.addListener(this::setChanged);
        return insn;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> PropertyInstance<T> getProperty(BlockEntityProperty<T> type) {
        return (PropertyInstance<T>) properties.get(type.getId());
    }

    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public static final class PropertyInstance<T> {
        private final BlockEntityProperty<T> type;

        private T value;
        final boolean synced;
        final boolean saved;
        private final List<Runnable> listeners = new CopyOnWriteArrayList<>();

        PropertyInstance(BlockEntityProperty<T> type, boolean synced, boolean saved) {
            this.type = type;
            this.synced = synced;
            this.saved = saved;
        }

        void read(Tag tag) {
            value = type.nbtDecoder().apply(tag);
        }
        Tag write() {
            return type.nbtEncoder().apply(value);
        }

        public T get() {
            return value;
        }
        public void set(T value) {
            this.value = value;
            listeners.forEach(Runnable::run);
        }
        @SuppressWarnings("unchecked")
        public void setUnsafe(Object value) {
            set((T) value);
        }

        public void addListener(Runnable runnable) {
            listeners.add(runnable);
        }

        public DataSlot createSlot(ToIntFunction<T> serializer, IntFunction<T> deserializer) {
            return new DataSlot() {
                @Override
                public int get() {
                    return serializer.applyAsInt(value);
                }

                @Override
                public void set(int pValue) {
                    PropertyInstance.this.set(deserializer.apply(pValue));
                }
            };
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        final var tag = super.getUpdateTag();
        writeProperties(tag, true, false);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (level != null && level.isClientSide() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            handleUpdateTag(Objects.requireNonNull(pkt.getTag()));
        }
    }

    public void sendCheckedUpdatePacket() {
        if (level != null && !level.isClientSide())
            sendUpdatePacket();
    }

    public void sendUpdatePacket() {
        if (level == null || level.isClientSide()) {
            Antsportation.LOGGER.warn("Update packet call requested from client side", new IllegalStateException());
        } else if (isRemoved()) {
            Antsportation.LOGGER.warn("Update packet call requested for removed tile", new IllegalStateException());
        } else {
//            AntsportationNetwork.sendToAllTracking(new UpdateTilePacket(this), this);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readProperties(tag, true, false);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        readProperties(pTag, false, true);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        writeProperties(pTag, false, true);
    }

    protected void writeProperties(CompoundTag tag, boolean synced, boolean saving) {
        properties.forEach((id, insn) -> {
            if (synced && !insn.synced) return;
            if (saving && !insn.saved) return;
            tag.put("property_" + insn.type.name(), insn.write());
        });
    }

    protected void readProperties(CompoundTag tag, boolean synced, boolean saving) {
        properties.forEach((id, insn) -> {
            if (synced && !insn.synced) return;
            if (saving && !insn.saved) return;
            insn.read(Objects.requireNonNull(tag.get("property_" + insn.type.name())));
        });
    }
}
