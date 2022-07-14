package com.matyrobbrt.antsportation.menu.boxing;

import com.matyrobbrt.antsportation.block.entity.boxing.UnboxerBE;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class UnboxerMenu extends BaseBoxingMenu<UnboxerBE> {

    public UnboxerMenu(int pContainerId, UnboxerBE tile, Inventory inventory) {
        super(AntsportationMenus.UNBOXER.get(), pContainerId, tile, inventory);
        canInsert = false;
    }
}
