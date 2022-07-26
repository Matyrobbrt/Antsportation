package com.matyrobbrt.antsportation.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface AntTarget {
    boolean isValidTarget(BlockState state, BlockPos pos, Level level);
}
