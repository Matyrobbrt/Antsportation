package com.matyrobbrt.antsportation.menu;

import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BoxMenu extends BaseMenu {
    public final ItemStack stack;

    public BoxMenu(int pContainerId, Inventory inventory, ItemStack stack) {
        super(AntsportationMenus.BOX.get(), inventory, pContainerId, 140);
        this.stack = stack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return pPlayer.getInventory().contains(stack);
    }
}
