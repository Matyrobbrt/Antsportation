package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.menu.BoxMenu;
import com.matyrobbrt.antsportation.menu.boxing.BoxerMenu;
import com.matyrobbrt.antsportation.menu.boxing.UnboxerMenu;
import com.mojang.datafixers.util.Function3;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registry.MENU_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<MenuType<BoxMenu>> BOX = MENUS.register("box", () -> IForgeMenuType.create((windowId, inv, data) ->
            new BoxMenu(windowId, inv, inv.player.getItemInHand(data.readEnum(InteractionHand.class)))));

    public static final RegistryObject<MenuType<BoxerMenu>> BOXER = MENUS.register("boxer", () -> IForgeMenuType.create(factory(BoxerMenu::new)));
    public static final RegistryObject<MenuType<UnboxerMenu>> UNBOXER = MENUS.register("unboxer", () -> IForgeMenuType.create(factory(UnboxerMenu::new)));
    public static final RegistryObject<MenuType<BoxerMenu.Configuration>> BOXER_CONFIGURATION = MENUS.register("boxer_configuration", () ->
            IForgeMenuType.create(factory(BoxerMenu.Configuration::new)));

    @SuppressWarnings("unchecked")
    private static <M extends AbstractContainerMenu, T extends BlockEntity> IContainerFactory<M> factory(Function3<Integer, T, Inventory, M> function) {
        return (windowId, inv, data) -> function.apply(windowId, (T) inv.player.level.getBlockEntity(data.readBlockPos()), inv);
    }
}
