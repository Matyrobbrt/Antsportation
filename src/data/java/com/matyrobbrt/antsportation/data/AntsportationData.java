package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.loot.LootProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Antsportation.MOD_ID)
public class AntsportationData {
    @SubscribeEvent
    @SuppressWarnings("unused")
    static void gatherData(final GatherDataEvent event) {
        final var gen = event.getGenerator();
        final var existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new Lang(gen));
            gen.addProvider(new Models(gen, existingFileHelper));
            gen.addProvider(new Sounds(gen, existingFileHelper));
        }
        if (event.includeServer()) {
            gen.addProvider(new Recipes(gen));
            gen.addProvider(new LootProvider(gen));

            final var blocks = new Tags.Blocks(gen, existingFileHelper);
            gen.addProvider(new Tags.Items(gen, blocks, existingFileHelper));
            gen.addProvider(blocks);
        }
    }
}
