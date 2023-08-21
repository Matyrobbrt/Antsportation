package com.matyrobbrt.antsportation.data

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.data.advancement.AdvancementGenerator
import com.matyrobbrt.antsportation.data.client.Lang
import com.matyrobbrt.antsportation.data.client.Models
import com.matyrobbrt.antsportation.data.client.Sounds
import com.matyrobbrt.antsportation.data.loot.LootProvider
import com.matyrobbrt.antsportation.data.patchouli.PatchouliProvider
import com.matyrobbrt.antsportation.data.server.BiomeModifiers
import com.matyrobbrt.antsportation.data.server.OneTimeJoins
import com.matyrobbrt.antsportation.data.server.Recipes
import com.matyrobbrt.antsportation.data.server.Tags
import com.mojang.serialization.JsonOps
import groovy.transform.CompileStatic
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.RegistryOps
import net.minecraftforge.common.data.JsonCodecProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries

// TODO - fix old datagen
@CompileStatic
@SuppressWarnings('unused')
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Antsportation.MOD_ID)
class AntsportationData {
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        final var gen = event.generator
        final var existingFileHelper = event.existingFileHelper

        // Client
        final var lang = new Lang(gen)
        gen.addProvider event.includeClient(), lang
        gen.addProvider event.includeClient(), lang.@enUd

        gen.addProvider event.includeClient(), new Models(gen, existingFileHelper)
        gen.addProvider event.includeClient(), new Sounds(gen, existingFileHelper)

        final var patchouli = new PatchouliProvider(gen)
        gen.addProvider event.includeClient(), patchouli
        gen.addProvider event.includeClient(), patchouli.@enUd


        // Server
        final var advancements = new AdvancementGenerator(gen, existingFileHelper)

        gen.addProvider event.includeServer(), new Recipes(gen)
        gen.addProvider event.includeServer(), advancements
        gen.addProvider event.includeServer(), new LootProvider(gen, advancements)

        final var blocks = new Tags.Blocks(gen, existingFileHelper)
        gen.addProvider event.includeServer(), new Tags.Items(gen, blocks, existingFileHelper)
        gen.addProvider event.includeServer(), new Tags.Entities(gen, existingFileHelper)
        gen.addProvider event.includeServer(), blocks

        gen.addProvider event.includeServer(), new OneTimeJoins(gen, existingFileHelper)

        final ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy())
        gen.addProvider event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                gen, existingFileHelper, Antsportation.MOD_ID, ops,
                ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers.asMap(ops)
        )
    }
}
