package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MarkerBE extends BlockEntity {

    private int sugarAmount = 0;
    private DyeColor color = DyeColor.WHITE;
    private static final String SUGAR_NBT_KEY = "sugar_amount";
    private static final String COLOR_NBT_KEY = "dye_color";
    public BlockPos nextMarker;

    public MarkerBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.MARKER_BE.get(), pWorldPosition, pBlockState);
    }

    public int getSugarAmount() {
        return sugarAmount;
    }

    public boolean increaseSugarAmount() {
        if (sugarAmount >= 10) {
            return false;
        }
        sugarAmount += 1;
        setChanged();
        level.markAndNotifyBlock(getBlockPos(), getLevel().getChunkAt(getBlockPos()), getBlockState(), getBlockState(), 3, 512);
        return true;
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
        return color!=DyeColor.WHITE;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        sugarAmount = nbt.getInt(SUGAR_NBT_KEY);
        color = DyeColor.byName(nbt.getString(COLOR_NBT_KEY), DyeColor.WHITE);
        nextMarker = NbtUtils.readBlockPos(nbt.getCompound("nextMarker"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt(SUGAR_NBT_KEY, sugarAmount);
        pTag.putString(COLOR_NBT_KEY, color.getName());
        if (nextMarker != null) {
            pTag.put("nextMarker", NbtUtils.writeBlockPos(nextMarker));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void checkMarker(AntWorkerEntity pEntity){
        if (level != null && level.getGameTime() % 5 == 0 && !level.isClientSide()) {
            if (nextMarker == null) {
                nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockPos() != this.getBlockPos() && entity.getBlockState().is(AntsportationBlocks.ANT_NEST.get()) && ((AntHillBE) entity).hasQueen, 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.ANT_HILL.get())) {
                nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockPos() != this.getBlockPos() &&  entity.getBlockState().is(AntsportationBlocks.ANT_NEST.get()) && ((AntHillBE) entity).hasQueen, 10).orElse(null);
            }
            if (nextMarker == null) {
                nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockPos() != this.getBlockPos() &&  entity.getBlockState().is(AntsportationBlocks.MARKER.get()), 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.MARKER.get())) {
                nextMarker = findNearestBlock(pEntity, level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockPos() != this.getBlockPos() &&  entity.getBlockState().is(AntsportationBlocks.MARKER.get()), 10).orElse(null);
            }
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

}
