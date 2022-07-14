package com.matyrobbrt.antsportation.menu.boxing;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.boxing.BaseBoxingBE;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BaseBoxingMenu<T extends BaseBoxingBE> extends AbstractContainerMenu {

    public final T tile;
    protected boolean canInsert = true;

    public BaseBoxingMenu(MenuType<?> menuType, int pContainerId, T tile, Inventory inventory) {
        super(menuType, pContainerId);
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
                    addSlot(new SlotItemHandler(tile.getInventory(), j + i * 5, xStart + j * 18, yStart + i * 18) {
                        @Override
                        public boolean mayPlace(@NotNull ItemStack stack) {
                            if (!canInsert)
                                return false;
                            return super.mayPlace(stack);
                        }
                    });
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

    @Override
    public boolean stillValid(Player pPlayer) {
        return Utils.checkTileStillValid(pPlayer, tile);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // TODO: learwin, pls fix
        // boxes go in slot 0
        // speed upgrades in slot 1
        // rest goes in slots 2 -> 16

        var itemstack = ItemStack.EMPTY;
        var slot = slots.get(index);
        if (slot.hasItem()) {
            var itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final var INVENTORYSIZE = 17;
            final var PLAYERINVENTORYEND = INVENTORYSIZE + 27;
            final var PLAYERHOTBAREND = PLAYERINVENTORYEND + 9;

            if (index < INVENTORYSIZE) {
                if (!moveItemStackTo(itemstack1, INVENTORYSIZE, PLAYERHOTBAREND, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof BoxItem) {
                    if (!moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.is(AntsportationItems.SPEED_UPGRADE.get())) {
                    if (!moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!moveItemStackTo(itemstack1, 2, 17, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }
}
