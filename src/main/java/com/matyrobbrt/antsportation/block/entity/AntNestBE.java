package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.DelegatingItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
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
            if (level != null && level.getGameTime() % IORATE == 0 && this.getStackInSlot(0).getCount() + stack.getCount() < this.getStackInSlot(0).getMaxStackSize()) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return ItemStack.EMPTY;
            }
        }
    });
    private final LazyOptional<IItemHandler> inventoryOutputLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (level != null && level.getGameTime() % IORATE == 0 && this.getStackInSlot(0).getCount() != 0){
                return super.extractItem(slot, amount, simulate);
            }else{
                return ItemStack.EMPTY;
            }
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
        //TODO: change to ant mount (still don't know if that's how you spell it)
        if(level != null && level.getGameTime() % IORATE == 0 && level.getBlockEntity(this.worldPosition.above()) instanceof BoxerBE blockEntity) {
            if (hasQueen) {
                pushToBlockAbove(blockEntity);
            }else{
                pullFromBlockAbove(blockEntity);
            }
        }
    }

    private void pullFromBlockAbove(BoxerBE blockEntity) {
        final ItemStack slot = inventory.getStackInSlot(0);
        if(slot.getCount() != 0 && slot.getCount() < slot.getMaxStackSize()) {
            int number = 0;
            for (ItemStack stack : blockEntity.inventory.getStacks()) {
                if (!stack.isEmpty()) {
                    ItemStack itemStack = stack.copy();
                    itemStack.setCount(1);
                    this.inventory.insertItem(0, itemStack, false);
                    blockEntity.inventory.extractItem(number, 1, false);
                }
                number++;
            }
        }
    }

    private void pushToBlockAbove(BoxerBE blockEntity){
        if(this.inventory.getStackInSlot(0).getCount() + 1 < inventory.getStackInSlot(0).getMaxStackSize()) {
            int number = 0;
            for (ItemStack stack : blockEntity.inventory.getStacks()) {
                if (!stack.isEmpty() && number < blockEntity.inventory.getSlots() - 1) {
                    number++;
                }
            }
            blockEntity.inventory.insertItem(number, inventory.getStackInSlot(0), false);
            this.inventory.extractItem(0, 1, false);
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

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (hasQueen) {
                if(side != Direction.UP) {
                    return inventoryInputLazy.cast();
                }
            }else{
                if(side != Direction.UP) {
                    return inventoryOutputLazy.cast();
                }
            }
        }
        return super.getCapability(cap, side);
    }

    class Inventory extends ItemStackHandler {
        public Inventory() {
            super(1);
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
            AntNestBE.this.setChanged();
        }
    }
}