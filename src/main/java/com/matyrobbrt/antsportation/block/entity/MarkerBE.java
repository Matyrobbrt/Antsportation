package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.compat.top.TOPContext;
import com.matyrobbrt.antsportation.compat.top.TOPInfoDriver;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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

@SuppressWarnings("ConstantConditions")
@ParametersAreNonnullByDefault
public class MarkerBE extends BlockEntity implements TOPInfoDriver {

    private static final int SUGARAMOUNT = 10;
    private DyeColor color = DyeColor.WHITE;
    private static final String COLOR_NBT_KEY = "dye_color";
    public BlockPos nextMarker;
    private int antCount = 0;
    public final List<UUID> ants = new ArrayList<>();

    public static final Predicate<BlockEntity> IS_O_HILL = (entity) -> entity instanceof AntHillBE antHillBE && !antHillBE.hasQueen;

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        color = DyeColor.byName(nbt.getString(COLOR_NBT_KEY), DyeColor.WHITE);
        nextMarker = NbtUtils.readBlockPos(nbt.getCompound("nextMarker"));
        antCount = nbt.getInt("antCount");

        ants.clear();
        nbt.getList("ants", Tag.TAG_INT_ARRAY)
                .stream().map(NbtUtils::loadUUID)
                .forEach(ants::add);
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

    public void increaseAntCount() {
        antCount++;
        if (antCount >= Integer.MAX_VALUE) {
            antCount = 0;
        }
    }

    public void increaseNeighbourAntCount() {
        for (Direction dir : Direction.values()) {
            final BlockEntity blockEntity;
            blockEntity = level.getBlockEntity(getBlockPos().relative(dir));
            if (blockEntity instanceof MarkerBE marker) {
                marker.increaseAntCount();
            }
        }
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
        // First we try to find adjacent markers for splitting
        nextMarker = findAdjacentBlock(pEntity, level, this.getBlockPos(),
                (entity) -> entity instanceof MarkerBE marker &&
                        marker.getColor() != this.getColor() &&
                        marker.isColored() &&
                        marker.shouldReceiveAnt(
                                adjacentBlocks(this.getAdjacent(entity.getBlockPos()), level, this.getBlockPos())
                        ));
        // Notify neighbour markers about the ant we just met
        increaseNeighbourAntCount();

        // If we haven't found markers to split on, let's try an output hill
        if (nextMarker == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), IS_O_HILL, 10);
        }
        // No output hill? let's try a marker of the same colour
        if (nextMarker == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) ->
                            entity instanceof MarkerBE marker &&
                                    marker.getColor() == getColor(), 10);
        }
        // None of the above conditions match? then we go and try to find white markers
        if (nextMarker == null) {
            nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) ->
                            entity instanceof MarkerBE marker &&
                                    !marker.isColored(), 10);
        }
    }

    @Nullable
    @SuppressWarnings("DuplicatedCode")
    public static BlockPos findNearestBlock(@Nullable AntWorkerEntity pEntity, Level level, BlockPos searchPos, Predicate<BlockEntity> predicate, double pDistance) {
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        if (pEntity != null)
            predicate = predicate.and(e -> !pEntity.nodeHistory.contains(pos));

        for (int i = 0; (double) i <= pDistance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; (double) j < pDistance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        pos.setWithOffset(searchPos, k, i - 1, l);
                        if (searchPos.closerThan(pos, pDistance) && !pos.equals(searchPos) && predicate.test(level.getBlockEntity(pos))) {
                            return pos;
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public BlockPos findAdjacentBlock(AntWorkerEntity pEntity, Level level, BlockPos searchPos, Predicate<BlockEntity> predicate) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            blockPos.setWithOffset(searchPos, dir);
            if (predicate.test(level.getBlockEntity(blockPos)) && !pEntity.nodeHistory.contains(blockPos)) {
                return blockPos;
            }
        }
        return null;
    }

    public Direction getAdjacent(BlockPos searchPos) {
        for (Direction dir : Direction.values()) {
            if (searchPos.relative(dir).compareTo(this.getBlockPos()) == 0) {
                return dir;
            }
        }
        return null;
    }

    public int adjacentBlocks(Direction direction, Level level, BlockPos searchPos) {
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

    @Override
    public void addInfo(TOPContext context) {
        context.text(Translations.TOP_MARKER_COLOUR.translate(
                Utils.textComponent(getColor().getName().replace('_', ' '))
                        .withStyle(s -> s.withColor(getColor().getTextColor()))
        ));
    }
}