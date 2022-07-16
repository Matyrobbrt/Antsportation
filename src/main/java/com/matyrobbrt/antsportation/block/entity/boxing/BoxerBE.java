package com.matyrobbrt.antsportation.block.entity.boxing;

import com.matyrobbrt.antsportation.block.entity.HasMultipleMenus;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.menu.boxing.BoxerMenu;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.cap.DelegatingItemHandler;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxerBE extends BaseBoxingBE implements MenuProvider, HasMultipleMenus {
    public BoxerBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.BOXER_BE.get(), pWorldPosition, pBlockState);
    }

    protected boolean isBoxLocked;
    public int releasePercent = 0;
    public RedstoneControl redstoneControl = RedstoneControl.DISABLED;
    public final Inventory inventory = new Inventory();

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    protected final MenuProvider configurationMenu = new MenuProvider() {
        @Override
        public Component getDisplayName() {
            return Translations.CONFIGURATION_GUI.translate(BoxerBE.this.getDisplayName());
        }

        @Override
        public AbstractContainerMenu createMenu(int pContainerId, net.minecraft.world.entity.player.Inventory pInventory, Player pPlayer) {
            return new BoxerMenu.Configuration(pContainerId, BoxerBE.this, pInventory);
        }
    };

    @Nullable
    @Override
    public MenuProvider getMenu(byte index) {
        return index == 1 ? configurationMenu : null;
    }

    protected class Inventory extends ItemStackHandler {
        public Inventory() {
            super(15);
        }

        protected NonNullList<ItemStack> getStacks() {
            return stacks;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return !(stack.getItem() instanceof BoxItem);
        }

        @Override
        protected void onContentsChanged(int slot) {
            BoxerBE.this.setChanged();
        }
    }

    private int lastSignal;

    public void tick() {
        if (level == null)
            return;
        isBoxLocked = !(switch (redstoneControl) {
            case DISABLED -> false;
            case LOW -> level.getBestNeighborSignal(worldPosition) <= 0;
            case HIGH -> level.getBestNeighborSignal(worldPosition) >= 1;
            case PULSE -> {
                final var currentSignal = level.getBestNeighborSignal(worldPosition);
                if (currentSignal == lastSignal) {
                    yield false;
                }
                lastSignal = currentSignal;
                yield true;
            }
        });

        final var box = this.box.getStackInSlot(0);
        manipulateProgress(inventory.getStacks().stream().allMatch(ItemStack::isEmpty) || box.isEmpty() || BoxItem.isFull(box));

        if (!box.isEmpty()) {
            if (releasePercent > 0) {
                final var fillage = (BoxItem.getStoredCount(box) / ((BoxItem) box.getItem()).tier().space) * 100;
                if (fillage >= releasePercent)
                    isBoxLocked = false;
            }
            if (progress >= maxProgress) {
                final var madeProgress = new AtomicBoolean();
                IntStream.range(0, inventory.getSlots())
                        .mapToObj(i -> new StackInstance(i, inventory.getStackInSlot(i)))
                        .filter(in -> !in.stack().isEmpty())
                        .limit(3)
                        .forEach(stack -> {
                            final var result = BoxItem.load(box, stack.stack());
                            if (result.getCount() < stack.stack().getCount())
                                madeProgress.set(true);
                            inventory.setStackInSlot(stack.slot(), result);
                        });
                if (madeProgress.get()) {
                    whenProgressMade();
                }
            }
        }
    }

    protected record StackInstance(int slot, ItemStack stack) {}

    private final LazyOptional<IItemHandler> inventoryLazy = LazyOptional.of(() -> new DelegatingItemHandler(inventory));
    private final LazyOptional<IItemHandler> boxLazy = LazyOptional.of(() -> new DelegatingItemHandler(box) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (isBoxLocked) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }
    });

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != null && side.getAxis() == Direction.Axis.Y)
                return boxLazy.cast();
            return inventoryLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        releasePercent = nbt.getInt("releasePercent");
        redstoneControl = RedstoneControl.values()[nbt.getInt("redstoneControl")];
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("releasePercent", releasePercent);
        pTag.putInt("redstoneControl", redstoneControl.ordinal());
    }

    @Override
    public Component getDisplayName() {
        return Translations.BOXER.translate();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, net.minecraft.world.entity.player.Inventory pInventory, Player pPlayer) {
        return new BoxerMenu(pContainerId, this, pInventory);
    }

}
