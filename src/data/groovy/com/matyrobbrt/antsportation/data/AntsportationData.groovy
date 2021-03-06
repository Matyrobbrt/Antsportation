package com.matyrobbrt.antsportation.data

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.data.advancement.AdvancementGenerator
import com.matyrobbrt.antsportation.data.client.Lang
import com.matyrobbrt.antsportation.data.client.Models
import com.matyrobbrt.antsportation.data.client.Sounds
import com.matyrobbrt.antsportation.data.loot.LootProvider
import com.matyrobbrt.antsportation.data.patchouli.PatchouliProvider
import com.matyrobbrt.antsportation.data.server.OneTimeJoins
import com.matyrobbrt.antsportation.data.server.Recipes
import com.matyrobbrt.antsportation.data.server.Tags
import groovy.transform.CompileStatic
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent

@CompileStatic
@SuppressWarnings('unused')
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Antsportation.MOD_ID)
class AntsportationData {
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        final var gen = event.generator
        final var existingFileHelper = event.existingFileHelper

        if (event.includeClient()) {
            final var lang = new Lang(gen)
            gen.addProvider lang
            gen.addProvider lang.@enUd

            gen.addProvider new Models(gen, existingFileHelper)
            gen.addProvider new Sounds(gen, existingFileHelper)

            final var patchouli = new PatchouliProvider(gen)
            gen.addProvider patchouli
            gen.addProvider patchouli.@enUd
        }
        if (event.includeServer()) {
            final var advancements = new AdvancementGenerator(gen, existingFileHelper)

            gen.addProvider new Recipes(gen)
            gen.addProvider(advancements)
            gen.addProvider new LootProvider(gen, advancements)

            final var blocks = new Tags.Blocks(gen, existingFileHelper)
            gen.addProvider new Tags.Items(gen, blocks, existingFileHelper)
            gen.addProvider new Tags.Entities(gen, existingFileHelper)
            gen.addProvider blocks

            gen.addProvider new OneTimeJoins(gen, existingFileHelper)
        }
    }
}
