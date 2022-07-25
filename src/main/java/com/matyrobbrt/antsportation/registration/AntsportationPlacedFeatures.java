package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class AntsportationPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<PlacedFeature> ANT_NEST_PLACED = PLACED_FEATURES.register("ant_nest_placed",
            () -> new PlacedFeature(AntsportationConfiguredFeatures.ANT_NEST.getHolder().orElseThrow(), List.of(RarityFilter.onAverageOnceEvery(12), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
}
