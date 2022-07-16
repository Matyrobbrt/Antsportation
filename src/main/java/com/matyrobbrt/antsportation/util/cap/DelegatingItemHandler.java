package com.matyrobbrt.antsportation.util.cap;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class DelegatingItemHandler implements IItemHandler {
    private final IItemHandler target;

    public DelegatingItemHandler(IItemHandler target) {
        this.target = target;
    }

    @Override
    public int getSlots() {
        return target.getSlots();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return target.getStackInSlot(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return target.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return target.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return target.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return target.isItemValid(slot, stack);
    }
}
