package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class AntJarBlock extends Block {
    public static final BooleanProperty ANTINSIDE = BooleanProperty.create("antinside");

    public AntJarBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(ANTINSIDE, false));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(ANTINSIDE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ANTINSIDE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide() && pState.getValue(ANTINSIDE)) {
            AntQueenEntity entity = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), pLevel);
            entity.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
            pLevel.addFreshEntity(entity);
        }
    }
}
