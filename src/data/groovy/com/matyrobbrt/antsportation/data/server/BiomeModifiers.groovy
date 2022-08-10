package com.matyrobbrt.antsportation.data.server

import com.google.gson.JsonElement
import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.registration.AntsportationEntities
import com.matyrobbrt.antsportation.registration.AntsportationPlacedFeatures
import groovy.transform.CompileStatic
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers

import java.util.function.BiConsumer

@CompileStatic
class BiomeModifiers  {
    static void accept(RegistryOps<JsonElement> ops, BiConsumer<String, BiomeModifier> consumer) {
        final forest = new HolderSet.Named<Biome>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), BiomeTags.IS_FOREST)
        consumer.accept('ant_nests', new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                forest,
                HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).orElseThrow().getHolder(AntsportationPlacedFeatures.ANT_NEST_PLACED.getKey()).orElseThrow()),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ))
        consumer.accept('ant_queens', new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                forest, List.of(
                new SpawnerData(AntsportationEntities.ANT_SOLDIER.get(), 1400, 1, 4),
                new SpawnerData(AntsportationEntities.ANT_QUEEN.get(), 800, 1, 1)
        )))
    }

    static Map<ResourceLocation, BiomeModifier> asMap(RegistryOps<JsonElement> ops) {
        final Map<ResourceLocation, BiomeModifier> map = [:]
        accept(ops, (name, modifier) -> map[Antsportation.rl(name)] = modifier)
        return map
    }
}
