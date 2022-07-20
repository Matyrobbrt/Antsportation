package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AntJarBE extends BlockEntity {
    public AntJarBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_JAR_BE.get(), pWorldPosition, pBlockState);
    }
}
