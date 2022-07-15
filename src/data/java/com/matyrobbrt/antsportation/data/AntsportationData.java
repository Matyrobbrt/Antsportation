package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.client.Lang;
import com.matyrobbrt.antsportation.data.client.Models;
import com.matyrobbrt.antsportation.data.client.Sounds;
import com.matyrobbrt.antsportation.data.loot.LootProvider;
import com.matyrobbrt.antsportation.data.patchouli.PatchouliProvider;
import com.matyrobbrt.antsportation.data.server.Recipes;
import com.matyrobbrt.antsportation.data.server.Tags;
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
            final var lang = new Lang(gen);
            gen.addProvider(lang);
            gen.addProvider(lang.enUd);

            gen.addProvider(new Models(gen, existingFileHelper));
            gen.addProvider(new Sounds(gen, existingFileHelper));

            final var patchouli = new PatchouliProvider(gen);
            gen.addProvider(patchouli);
            gen.addProvider(patchouli.enUd);
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
