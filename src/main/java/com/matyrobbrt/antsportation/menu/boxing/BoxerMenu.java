package com.matyrobbrt.antsportation.menu.boxing;

import com.matyrobbrt.antsportation.block.entity.boxing.BoxerBE;
import com.matyrobbrt.antsportation.menu.BaseMenu;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.matyrobbrt.antsportation.util.RedstoneControl;
import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxerMenu extends BaseBoxingMenu<BoxerBE> {

    public BoxerMenu(int pContainerId, BoxerBE tile, Inventory inventory) {
        super(AntsportationMenus.BOXER.get(), pContainerId, tile, inventory);
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
            return Utils.checkTileStillValid(pPlayer, tile);
        }
    }
}
