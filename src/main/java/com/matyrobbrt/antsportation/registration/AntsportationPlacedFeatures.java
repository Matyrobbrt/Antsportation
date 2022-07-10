package com.matyrobbrt.antsportation.registration;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class AntsportationPlacedFeatures {
    public static Holder<PlacedFeature> ANT_NEST_PLACED = PlacementUtils.register("ant_nest_placed", AntsportationConfiguredFeatures.ANT_NEST, RarityFilter.onAverageOnceEvery(8), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
}
