package com.matyrobbrt.antsportation.menu;

import com.matyrobbrt.antsportation.block.entity.BoxerBE;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxerMenu extends AbstractContainerMenu {
    public final BoxerBE tile;

    public BoxerMenu(int pContainerId, BoxerBE tile, Inventory inventory) {
        super(AntsportationMenus.BOXER.get(), pContainerId);
        this.tile = tile;

        {
            // Tile inv: upgrades + box
            addSlot(new SlotItemHandler(tile.box, 0, 130 + 5, 35 + 5));
            addSlot(new SlotItemHandler(tile.upgrades, 0, 152, 77));
        }

        {
            // Tile inv: input
            final var yStart = 21;
            final var xStart = 8;
            // Go down
            for (int i = 0; i < 3; i++) {
                // Go sideways
                for (int j = 0; j < 5; j++) {
                    addSlot(new SlotItemHandler(tile.inventory, j + i * 5, xStart + j * 18, yStart + i * 18));
                }
            }
        }

        {
            // Player inv
            final var yOffset = 97;
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, yOffset + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k, 8 + k * 18, yOffset + 58));
            }
        }

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return tile.maxProgress;
            }

            @Override
            public void set(int pValue) {
                tile.maxProgress = pValue;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return tile.progress;
            }

            @Override
            public void set(int pValue) {
                tile.progress = pValue;
            }
        });
        setupEnergyData();
    }

    private void setupEnergyData() {
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return tile.energy.getMaxEnergyStored() & 0xffff;
            }

            @Override
            public void set(int value) {
                final var cap = tile.energy.getMaxEnergyStored() & 0xffff0000;
                tile.energy.setCapacity(cap + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (tile.energy.getMaxEnergyStored() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                final int capacity = tile.energy.getMaxEnergyStored() & 0x0000ffff;
                tile.energy.setCapacity(capacity | (value << 16));
            }
        });

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return tile.energy.getEnergyStored() & 0xffff;
            }

            @Override
            public void set(int value) {
                final int energyStored = tile.energy.getEnergyStored() & 0xffff0000;
                tile.energy.setAmount(energyStored + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (tile.energy.getEnergyStored() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                final int energyStored = tile.energy.getEnergyStored() & 0x0000ffff;
                tile.energy.setAmount(energyStored | (value << 16));
            }
        });
    }

    public int getProgressionScaled() {
        return tile.progress != 0 && tile.maxProgress != 0
                ? tile.progress * 24 / tile.maxProgress
                : 0;
    }

    public int getEnergyScaled() {
        return tile.energy.getEnergyStored() != 0 && tile.energy.getMaxEnergyStored() != 0
                ? tile.energy.getEnergyStored() * 88 / tile.energy.getMaxEnergyStored()
                : 0;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return checkTileStillValid(pPlayer, tile);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        final var slotAmount = 27 + 15 + 2;
        if (slot != null && slot.hasItem()) {
            final var newStack = slot.getItem();
            itemstack = newStack.copy();
            if (pIndex >= 0 && pIndex < slotAmount) {
                if (!this.moveItemStackTo(newStack, slotAmount, slotAmount + 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= slotAmount && pIndex < slotAmount + 9 && !this.moveItemStackTo(newStack, 0, slotAmount, false)) {
                return ItemStack.EMPTY;
            }

            if (newStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (newStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(pPlayer, newStack);
            this.broadcastChanges();
        }

        return itemstack;
    }

    public static final class Configuration extends BaseMenu {
        public final BoxerBE tile;

        public Configuration(int pContainerId, BoxerBE tile, Inventory inventory) {
            super(AntsportationMenus.BOXER_CONFIGURATION.get(), inventory, pContainerId, 84);
            this.tile = tile;
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return tile.releasePercent;
                }

                @Override
                public void set(int value) {
                    tile.releasePercent = value;
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return tile.redstoneControl.ordinal();
                }

                @Override
                public void set(int pValue) {
                    tile.redstoneControl = RedstoneControl.values()[pValue];
                }
            });
            broadcastChanges();
        }

        @Override
        public boolean stillValid(Player pPlayer) {
            return checkTileStillValid(pPlayer, tile);
        }
    }

    private static boolean checkTileStillValid(Player pPlayer, @Nullable BlockEntity tile) {
        if (tile == null)
            return false;
        if (pPlayer.level.getBlockEntity(tile.getBlockPos()) != tile) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double) tile.getBlockPos().getX() + 0.5D, (double) tile.getBlockPos().getY() + 0.5D, (double) tile.getBlockPos().getZ() + 0.5D) > 64.0D);
        }
    }
}
