package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class MarkerBE extends BlockEntity {

    private static final int SUGARAMOUNT = 10;
    private DyeColor color = DyeColor.WHITE;
    private static final String COLOR_NBT_KEY = "dye_color";
    public BlockPos nextMarker;
    private int antCount = 0;
    public List<UUID> ants = new ArrayList<>();

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        color = DyeColor.byName(nbt.getString(COLOR_NBT_KEY), DyeColor.WHITE);
        nextMarker = NbtUtils.readBlockPos(nbt.getCompound("nextMarker"));
        antCount = nbt.getInt("antCount");
        ants = nbt.getList("ants", 11).stream().map(NbtUtils::loadUUID).collect(Collectors.toList());
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString(COLOR_NBT_KEY, color.getName());
        if (nextMarker != null) {
            pTag.put("nextMarker", NbtUtils.writeBlockPos(nextMarker));
        }
        pTag.putInt("antCount", antCount);
        ListTag listtag = new ListTag();
        ants.forEach((ant) -> listtag.add(NbtUtils.createUUID(ant)));
        pTag.put("ants", listtag);
    }

    public int getAntCount() {
        return antCount;
    }

    public void increaseAntCount() {
        antCount++;
    }

    public void increaseNeighbourAntCount() {
        for (Direction dir : Direction.values()) {
            final BlockEntity blockEntity;
            blockEntity = level.getBlockEntity(getBlockPos().relative(dir));
            if (blockEntity != null && blockEntity.getBlockState().is(AntsportationBlocks.MARKER.get())) {
                ((MarkerBE) blockEntity).increaseAntCount();
            }
        }
    }

    public void setAntCount(int count) {
        antCount = count;
    }

    public boolean shouldReceiveAnt(int offset) {
        try {
            return (antCount + offset) % (1 / (SUGARAMOUNT * 5f / 100f)) == 0;
        } catch (ArithmeticException error) {
            return false;
        }
    }

    public MarkerBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.MARKER_BE.get(), pWorldPosition, pBlockState);
    }

    public int getSugarAmount() {
        return SUGARAMOUNT;
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
        setChanged();
        level.markAndNotifyBlock(getBlockPos(), getLevel().getChunkAt(getBlockPos()), getBlockState(), getBlockState(), 3, 512);
    }

    public boolean isColored() {
        return color != DyeColor.WHITE;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        super.onDataPacket(net, pkt);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void checkMarker(AntWorkerEntity pEntity) {
        if (level == null || level.isClientSide()) {
            return;
        }
        if (this.ants.size() > 10) {
            ants.clear();
        }
        nextMarker = findAdjacentBlock(pEntity, level, this.getBlockPos(),
                (entity) -> entity instanceof MarkerBE marker &&
                        marker.getColor() != this.getColor() &&
                        marker.isColored() &&
                        marker.shouldReceiveAnt(
                                adjacentBlocks(this.getAdjacent(pEntity, level, entity.getBlockPos()), pEntity, level, this.getBlockPos())
                        ))
                .orElse(null);
        increaseNeighbourAntCount();

        if (nextMarker == null && findAdjacentBlock(pEntity, level, this.getBlockPos(),
                (entity) -> entity instanceof MarkerBE marker &&
                        getColor() != marker.getColor() &&
                        marker.isColored()
        ).orElse(null) == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity instanceof AntHillBE antHill && !antHill.hasQueen, 10).orElse(null);

        }
        if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.ANT_HILL.get()) && !level.getBlockState(nextMarker).is(AntsportationBlocks.MARKER.get())) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity instanceof AntHillBE antHillBE && !antHillBE.hasQueen, 10).orElse(null);
        }
        if (nextMarker == null && findAdjacentBlock(pEntity, level, this.getBlockPos(),
                (entity) -> entity instanceof MarkerBE marker &&
                        marker.getColor() != this.getColor() &&
                        marker.isColored()
        ).orElse(null) == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) ->
                            entity instanceof MarkerBE marker && !pEntity.nodeHistory.contains(entity.getBlockPos()) &&
                                    marker.getColor() == getColor()
                    , 10).orElse(null);
        }
        if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.ANT_HILL.get()) && !level.getBlockState(nextMarker).is(AntsportationBlocks.MARKER.get())) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) ->
                            entity instanceof MarkerBE marker && !pEntity.nodeHistory.contains(entity.getBlockPos()) &&
                                    marker.getColor() == getColor()
                    , 10).orElse(null);
        } else if (nextMarker == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) ->
                            entity instanceof MarkerBE marker && !pEntity.nodeHistory.contains(entity.getBlockPos()) &&
                                    !marker.isColored()
                    , 10).orElse(null);
        }
    }

    public Optional<BlockPos> findNearestBlock(AntWorkerEntity pEntity, Level level, BlockPos searchPos, Predicate<BlockEntity> p_28076_, double pDistance) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = 0; (double) i <= pDistance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; (double) j < pDistance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutableblockpos.setWithOffset(searchPos, k, i - 1, l);
                        if (searchPos.closerThan(blockpos$mutableblockpos, pDistance) && p_28076_.test(level.getBlockEntity(blockpos$mutableblockpos)) && !pEntity.nodeHistory.contains(blockpos$mutableblockpos)) {
                            return Optional.of(blockpos$mutableblockpos);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    public Optional<BlockPos> findAdjacentBlock(AntWorkerEntity pEntity, Level level, BlockPos searchPos, Predicate<BlockEntity> predicate) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            blockPos.setWithOffset(searchPos, dir);
            if (predicate.test(level.getBlockEntity(blockPos)) && !pEntity.nodeHistory.contains(blockPos)) {
                return Optional.of(blockPos);
            }
        }
        return Optional.empty();
    }

    public Direction getAdjacent(AntWorkerEntity pEntity, Level level, BlockPos searchPos) {
        for (Direction dir : Direction.values()) {
            if (searchPos != null && searchPos.relative(dir).compareTo(this.getBlockPos()) == 0) {
                return dir;
            }
        }
        return null;
    }

    public int adjacentBlocks(Direction direction, AntWorkerEntity pEntity, Level level, BlockPos searchPos) {
        List<Direction> blocks = new ArrayList<>();
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            blockPos.setWithOffset(searchPos, dir);
            if (level.getBlockEntity(blockPos) != null && level.getBlockEntity(blockPos).getBlockState().is(AntsportationBlocks.MARKER.get())) {
                blocks.add(dir);
            }
        }
        if (direction == null) {
            return 0;
        }
        return blocks.indexOf(direction.getOpposite());
    }

}