package com.matyrobbrt.antsportation.block.entity.boxing;

import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.menu.boxing.UnboxerMenu;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.cap.CombiningItemHandler;
import com.matyrobbrt.antsportation.util.cap.DelegatingItemHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class UnboxerBE extends BaseBoxingBE implements MenuProvider {
    public UnboxerBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.UNBOXER_BE.get(), pWorldPosition, pBlockState);
    }

    public final Inventory inventory = new Inventory();

    protected class Inventory extends ItemStackHandler {
        public Inventory() {
            super(15);
        }

        @Override
        protected void onContentsChanged(int slot) {
            UnboxerBE.this.setChanged();
        }
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    private static final int MAX_EXTRACT = 3 * 64;

    public void tick() {
        if (level == null)
            return;
        final var box = this.box.getStackInSlot(0);
        manipulateProgress(box.isEmpty() || BoxItem.getStoredCount(box) <= 0);

        if (!box.isEmpty()) {
            if (progress >= maxProgress) {
                final var madeProgress = new AtomicBoolean();
                final var insertedCount = new AtomicInteger();
                BoxItem.getStoredItems(box)
                        .limit(3)
                        .forEach(stack -> {
                            if (insertedCount.get() >= MAX_EXTRACT)
                                return;
                            final var actualStack = stack.getStack();
                            if (actualStack.getCount() > MAX_EXTRACT)
                                actualStack.setCount(MAX_EXTRACT);
                            if (actualStack.getCount() > MAX_EXTRACT - insertedCount.get())
                                actualStack.setCount(MAX_EXTRACT - insertedCount.get());
                            final var result = ItemHandlerHelper.insertItem(inventory, actualStack, true);
                            if (result.isEmpty() || result.getCount() != actualStack.getCount()) {
                                final var extracted = actualStack.getCount() - ItemHandlerHelper.insertItem(inventory, actualStack, false).getCount();
                                stack.extract(extracted);
                                insertedCount.set(extracted);
                                madeProgress.set(true);
                            }
                        });
                if (madeProgress.get()) {
                    whenProgressMade();
                }
            }
        }
    }

    private final LazyOptional<IItemHandler> inventoryLazy = LazyOptional.of(() -> new CombiningItemHandler(List.of(
            new DelegatingItemHandler(box) {
                @Override
                public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                    if (BoxItem.getStoredCount(getStackInSlot(0)) > 0)
                        return ItemStack.EMPTY;
                    return super.extractItem(slot, amount, simulate);
                }
            },
            new DelegatingItemHandler(inventory) {
                @Override
                public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                    return stack;
                }
            }
    )));

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Translations.UNBOXER.translate();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, net.minecraft.world.entity.player.Inventory pInventory, Player pPlayer) {
        return new UnboxerMenu(pContainerId, this, pInventory);
    }

    @Override
    protected boolean isReversed() {
        return true;
    }
}
