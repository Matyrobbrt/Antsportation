package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class AntHillBE extends BlockEntity {
    public final AntHillBE.Inventory inventory = new Inventory();
    private static final int SPAWNRATE = 300;
    public boolean hasQueen = false;
    public BlockPos nextMarker;


    public AntHillBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_HILL_BE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        hasQueen = nbt.getBoolean("hasQueen");
        nbt.getCompound("nextMarker");
        nextMarker = NbtUtils.readBlockPos(nbt.getCompound("nextMarker"));
    }

    //Temporary functionality TODO: do the ant spawning when we get that far
    public void tick() {
        if (level != null && hasQueen && level.getGameTime() % SPAWNRATE == 0 && !level.isClientSide()) {
            if (nextMarker == null) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockState().is(AntsportationBlocks.ANT_NEST.get()) && ((AntHillBE) entity).hasQueen, 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.ANT_HILL.get())) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockState().is(AntsportationBlocks.ANT_NEST.get()) && ((AntHillBE) entity).hasQueen, 10).orElse(null);
            }
            if (nextMarker == null) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockState().is(AntsportationBlocks.MARKER.get()), 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.MARKER.get())) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), (entity) -> entity != null && entity.getBlockState().is(AntsportationBlocks.MARKER.get()), 10).orElse(null);
            }
            if (nextMarker != null) {
                AntWorkerEntity antWorker = new AntWorkerEntity(AntsportationEntities.ANT_WORKER.get(), level);
                antWorker.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5);
                antWorker.setNextMarker(nextMarker);
                level.addFreshEntity(antWorker);
            }
        }
    }

    public Optional<BlockPos> findNearestBlock(Level level, BlockPos searchPos, Predicate<BlockEntity> p_28076_, double pDistance) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = 0; (double) i <= pDistance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; (double) j < pDistance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutableblockpos.setWithOffset(searchPos, k, i - 1, l);
                        if (searchPos.closerThan(blockpos$mutableblockpos, pDistance) && p_28076_.test(level.getBlockEntity(blockpos$mutableblockpos))) {
                            return Optional.of(blockpos$mutableblockpos);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", inventory.serializeNBT());
        pTag.putBoolean("hasQueen", hasQueen);
        if (nextMarker != null) {
            pTag.put("nextMarker", NbtUtils.writeBlockPos(nextMarker));
        }
    }

    private void dropContents(IItemHandler handler) {
        if (level == null)
            return;
        for (int i = 0; i < handler.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), handler.getStackInSlot(i));
        }
    }

    public void dropContents() {
        dropContents(inventory);
    }

    class Inventory extends ItemStackHandler {
        public Inventory() {
            super(10);
        }

        protected NonNullList<ItemStack> getStacks() {
            return stacks;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            AntHillBE.this.setChanged();
        }
    }
}
