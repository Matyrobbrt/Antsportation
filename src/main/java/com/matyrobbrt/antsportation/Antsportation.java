package com.matyrobbrt.antsportation;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Antsportation.MOD_ID)
public class Antsportation {
    public static final String MOD_ID = "antsportation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Antsportation() {
        LOGGER.debug("Initialising Antsportation");
        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        AntsportationBlocks.BLOCKS.register(bus);
        AntsportationItems.ITEMS.register(bus);
        AntsportationMenus.MENUS.register(bus);
        AntsportationEntities.ENTITIES.register(bus);
        LOGGER.debug("Antsportation initialized");
    }

    public static final CreativeModeTab TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(), MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return Items.ACACIA_FENCE.getDefaultInstance(); // TODO tab icon
        }
    };
}
