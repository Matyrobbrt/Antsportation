package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class AntHillBE extends BlockEntity {
    public final AntHillBE.Inventory inventory = new Inventory();
    private static final int SPAWNRATE = 100;
    public boolean hasQueen = false;
    public BlockPos nextMarker;

    private static final Predicate<BlockEntity> IS_HILL = (entity) -> entity instanceof AntHillBE hill && hill.hasQueen;
    private static final Predicate<BlockEntity> IS_MARKER = (entity) -> entity != null && entity.getBlockState().is(AntsportationBlocks.MARKER.get());

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


    public void tick() {
        if (level != null && hasQueen && level.getGameTime() % SPAWNRATE == 0) {
            if (nextMarker == null) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), IS_HILL, 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.ANT_HILL.get())) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), IS_HILL, 10).orElse(null);
            }
            if (nextMarker == null) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), IS_MARKER, 10).orElse(null);
            }
            if (nextMarker != null && !level.getBlockState(nextMarker).is(AntsportationBlocks.MARKER.get())) {
                nextMarker = findNearestBlock(level, this.getBlockPos(), IS_MARKER, 10).orElse(null);
            }
            if (nextMarker != null) {
                for (int i = 0; i < inventory.getSlots(); i++) {
                    final var stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        AntWorkerEntity antWorker = new AntWorkerEntity(AntsportationEntities.ANT_WORKER.get(), level);
                        ItemStack itemStack = stack.copy();
                        itemStack.setCount(1);
                        antWorker.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
                        inventory.extractItem(i, 1, false);
                        antWorker.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5);
                        antWorker.setNextMarker(nextMarker);
                        antWorker.nodeHistory.add(this.getBlockPos());
                        level.addFreshEntity(antWorker);
                        setChanged();
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public Optional<BlockPos> findNearestBlock(Level level, BlockPos searchPos, Predicate<BlockEntity> p_28076_, double pDistance) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = 0; (double) i <= pDistance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; (double) j < pDistance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockpos$mutableblockpos.setWithOffset(searchPos, k, i - 1, l);
                        if (searchPos.closerThan(blockpos$mutableblockpos, pDistance) && !searchPos.equals(blockpos$mutableblockpos) && p_28076_.test(level.getBlockEntity(blockpos$mutableblockpos))) {
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

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            AntHillBE.this.setChanged();
        }
    }

    public ItemStack addItem(ItemStack item){
        return ItemHandlerHelper.insertItem(inventory, item, false);
    }
}
