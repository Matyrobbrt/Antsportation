package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import com.matyrobbrt.antsportation.util.cap.DelegatingItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
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
import org.jetbrains.annotations.Nullable;

public class AntNestBE extends BlockEntity {
    public final AntNestBE.Inventory inventory = new Inventory();
    private static final int IORATE = 5;
    public boolean hasQueen = false;


    private final LazyOptional<IItemHandler> inventoryInputLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack.is(AntsportationTags.Items.BOXES) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final LazyOptional<IItemHandler> inventoryOutputLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    });

    public AntNestBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_NEST_BE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        nbt.getBoolean("hasQueen");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", inventory.serializeNBT());
        pTag.putBoolean("hasQueen", hasQueen);
    }

    public void tick() {
        if (getLevel().getGameTime() % IORATE == 0) {
            final var above = getLevel().getBlockEntity(worldPosition.above());
            if (above instanceof AntHillBE antHill) {
                if (hasQueen) {
                    pushToBlockAbove(antHill);
                } else {
                    pullFromBlockAbove(antHill);
                }
            }
        }
        hasQueen = getLevel().getBlockEntity(worldPosition.above()) instanceof AntHillBE blockEntity && blockEntity.hasQueen;
    }

    @NotNull
    @SuppressWarnings("ConstantConditions")
    public Level getLevel() {
        return level;
    }

    private void pullFromBlockAbove(AntHillBE blockEntity) {
        for (int i = 0; i < blockEntity.inventory.getSlots(); i++) {
            final var extracted = blockEntity.inventory.extractItem(i, 16, true);
            if (!extracted.isEmpty()) {
                final var remainder = ItemHandlerHelper.insertItem(inventory, extracted, false);
                blockEntity.inventory.extractItem(i, extracted.getCount() - remainder.getCount(), false);
            }
        }
    }

    private void pushToBlockAbove(AntHillBE blockEntity) {
        final var slot = inventory.getStackInSlot(0);
        if (slot.isEmpty()) return;
        final var remainder = ItemHandlerHelper.insertItem(blockEntity.inventory, slot, false);
        inventory.setStackInSlot(0, remainder);
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (hasQueen) {
                if (side != Direction.UP) {
                    return inventoryInputLazy.cast();
                }
            } else {
                if (side != Direction.UP) {
                    return inventoryOutputLazy.cast();
                }
            }
        }
        return super.getCapability(cap, side);
    }

    private class Inventory extends ItemStackHandler {
        public Inventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            AntNestBE.this.setChanged();
        }
    }
}
