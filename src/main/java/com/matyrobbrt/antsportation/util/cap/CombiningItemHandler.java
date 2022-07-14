package com.matyrobbrt.antsportation.util.cap;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CombiningItemHandler implements IItemHandler {
    private final List<SlotData> byIndex;

    public CombiningItemHandler(final List<IItemHandler> handlers) {
        this.byIndex = new ArrayList<>();
        for (final var handler : handlers) {
            for (int s = 0; s < handler.getSlots(); s++) {
                byIndex.add(new SlotData(s, handler));
            }
        }
    }

    @Override
    public int getSlots() {
        return byIndex.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int i) {
        validateSlotIndex(i);
        return byIndex.get(i).getStack();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean sim) {
        validateSlotIndex(slot);
        final var data = byIndex.get(slot);
        return data.handler().insertItem(data.relativeIndex(), itemStack, sim);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean sim) {
        validateSlotIndex(slot);
        final var data = byIndex.get(slot);
        return data.handler().extractItem(data.relativeIndex(), amount, sim);
    }

    @Override
    public int getSlotLimit(int i) {
        validateSlotIndex(i);
        return byIndex.get(i).getLimit();
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        validateSlotIndex(i);
        return byIndex.get(i).isValid(itemStack);
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.byIndex.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.byIndex.size() + ")");
        }
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public record SlotData(int relativeIndex, IItemHandler handler) {
        public ItemStack getStack() {
            return handler.getStackInSlot(relativeIndex);
        }
        public int getLimit() {
            return handler.getSlotLimit(relativeIndex);
        }
        public boolean isValid(ItemStack stack) {
            return handler.isItemValid(relativeIndex, stack);
        }
    }
}
