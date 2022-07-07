package com.matyrobbrt.antsportation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

public class MarkerBlock extends Block {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    public MarkerBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOL)
                .instabreak()
                .sound(SoundType.SAND)
                .noCollission()
                .color(MaterialColor.COLOR_BROWN));
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        var direction = pContext.getClickedFace();
        var state = switch (direction) {
            case UP -> defaultBlockState().setValue(FACING, Direction.UP);
            case DOWN -> defaultBlockState().setValue(FACING, Direction.DOWN);
            case EAST -> defaultBlockState().setValue(FACING, Direction.EAST);
            case WEST -> defaultBlockState().setValue(FACING, Direction.WEST);
            case SOUTH -> defaultBlockState().setValue(FACING, Direction.SOUTH);
            default -> defaultBlockState().setValue(FACING, Direction.NORTH);
        };
        if (state.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
            return state;
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        var direction = pState.getValue(FACING);
        var block = pPos.relative(direction.getOpposite());
        var supportBlockState = pLevel.getBlockState(block);
        return canSupportCenter(pLevel, block, direction);
    }
}
