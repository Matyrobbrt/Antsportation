package com.matyrobbrt.antsportation.world;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// TODO biome modifier
@Mod.EventBusSubscriber(modid = Antsportation.MOD_ID)
public class AntsportationWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        AntNestGen.generateNest(event);
        AntEntityGen.onEntitySpawn(event);
    }
}