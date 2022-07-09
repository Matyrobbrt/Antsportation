package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.AntNestBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class AntNestBlock extends BaseEntityBlock {
    public AntNestBlock(Properties p_49224_) {
        super(p_49224_);
    }

    private static <T extends BlockEntity> void tick(Level pLevel1, BlockPos pPos, BlockState pState1, T pBlockEntity) {
        ((AntNestBE) pBlockEntity).tick();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new AntNestBE(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : AntNestBlock::tick;
    }

    @Override
    public void onRemove(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof AntNestBE antNest) {
            antNest.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
