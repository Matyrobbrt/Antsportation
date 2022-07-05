package com.matyrobbrt.antsportation.client;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.screen.BoxScreen;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationItemProperties;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Antsportation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AntsportationClient {
    @SubscribeEvent
    static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForgeClient.registerTooltipComponentFactory(BoxItem.Tooltip.class, BoxTooltipClient::new);
        MenuScreens.register(AntsportationMenus.BOX.get(), BoxScreen::new);
        AntsportationItemProperties.addCustomItemProperties();
    }
}
