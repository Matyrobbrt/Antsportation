package com.matyrobbrt.antsportation.registration;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class AntsportationConfiguredFeatures {
    public static final Holder<ConfiguredFeature<BlockColumnConfiguration, ?>> ANT_NEST = FeatureUtils.register("ant_nest", Feature.BLOCK_COLUMN, new BlockColumnConfiguration(List.of(BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_NEST.get())), BlockColumnConfiguration.layer(ConstantInt.of(1), BlockStateProvider.simple(AntsportationBlocks.ANT_HILL.get()))), Direction.UP, BlockPredicate.anyOf(BlockPredicate.replaceable(), BlockPredicate.matchesTag(BlockTags.DIRT)), false));
}
