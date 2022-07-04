package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Antsportation.MOD_ID)
public class AntsportationData {
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        final var gen = event.getGenerator();
        final var existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(new Models(gen, existingFileHelper));
        gen.addProvider(new Recipes(gen));
        gen.addProvider(new Lang(gen));
    }
}
