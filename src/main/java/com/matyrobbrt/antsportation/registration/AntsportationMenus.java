package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.menu.BoxMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registry.MENU_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<MenuType<BoxMenu>> BOX = MENUS.register("box", () -> IForgeMenuType.create((windowId, inv, data) ->
            new BoxMenu(windowId, inv, inv.player.getItemInHand(data.readEnum(InteractionHand.class)))));
}
