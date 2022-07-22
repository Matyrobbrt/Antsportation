package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class AntsportationConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> ANT_NEST = CONFIGURED_FEATURES.register("ant_nest", () -> new ConfiguredFeature<>(Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_HILL.get())),BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_NEST.get()))), Direction.DOWN, BlockPredicate.anyOf(BlockPredicate.matchesBlocks(List.of(Blocks.AIR)), BlockPredicate.matchesTag(BlockTags.DIRT)), false)));
}
