package com.matyrobbrt.antsportation.world;

import com.matyrobbrt.antsportation.registration.AntsportationPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;

public class AntNestGen {
    public static void generateNest(final BiomeLoadingEvent event) {
        if (event.getName() == null) return;
        final ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        final var types = BiomeDictionary.getTypes(key);

        if (types.contains(BiomeDictionary.Type.FOREST)) {
            List<Holder<PlacedFeature>> base =
                    event.getGeneration().getFeatures(GenerationStep.Decoration.TOP_LAYER_MODIFICATION);

            base.add(AntsportationPlacedFeatures.ANT_NEST_PLACED.getHolder().orElseThrow());
        }
    }
}
