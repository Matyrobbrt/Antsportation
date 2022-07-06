package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.DelegatingItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
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
    private static final int INPUTRATE = 5;
    private long ticks = 0;


    private final LazyOptional<IItemHandler> inventoryInputLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (ticks % INPUTRATE == 0) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return ItemStack.EMPTY;
            }
        }
    });
    private final LazyOptional<IItemHandler> inventoryOutputLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    public boolean validStructure = false;

    public AntNestBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_NEST_BE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        validStructure = nbt.getBoolean("validStructure");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", inventory.serializeNBT());
        pTag.putBoolean("validStructure", validStructure);
    }

    public void tick() {
        ticks++;
        //TODO: change to ant mount (still dont know if thats how you spell it)
        if(level != null && !level.isClientSide() && ticks % INPUTRATE == 0 && level != null && level.getBlockEntity(this.worldPosition.above()) instanceof BoxerBE blockEntity){
            int number = 0;
            for (ItemStack stack : blockEntity.inventory.getStacks()){
                if(!stack.isEmpty() && number < blockEntity.inventory.getSlots()-1){
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
            if (side == Direction.UP)
                return inventoryOutputLazy.cast();
            return inventoryInputLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    protected class Inventory extends ItemStackHandler {
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
