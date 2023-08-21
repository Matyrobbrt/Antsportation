package com.matyrobbrt.antsportation.datanew

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationConfiguredFeatures
import groovy.transform.CompileDynamic
import net.minecraft.core.Direction
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@SuppressWarnings('unused')
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Antsportation.MOD_ID)
class AntsportationData {
    @CompileDynamic
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        final var gen = event.generator

        final nestKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Antsportation.MOD_ID, 'ant_nest'))
        final builder = new RegistrySetBuilder().add(Registries.PLACED_FEATURE) { ctx ->
            ctx.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Antsportation.MOD_ID, 'ant_nest_placed')), new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(nestKey), List.of(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())))
        }.add(Registries.CONFIGURED_FEATURE) { ctx ->
            ctx.register(nestKey, new ConfiguredFeature<>(Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_HILL.get())),
                    BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_NEST.get()))), Direction.DOWN, BlockPredicate.anyOf(BlockPredicate.matchesBlocks(List.of(Blocks.AIR)), BlockPredicate.matchesTag(BlockTags.DIRT)), false)))
        }
        gen.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(gen.packOutput, event.lookupProvider, builder, [Antsportation.MOD_ID] as Set))
    }
}