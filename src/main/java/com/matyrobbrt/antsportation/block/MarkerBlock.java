package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MarkerBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    private static final VoxelShape UP = Shapes.box(0.1875, 0.015625, 0.1875, 0.8125, 0.015635, 0.8125);
    private static final VoxelShape DOWN = Shapes.box(0.1875, 0.984365, 0.1875, 0.8125, 0.984375, 0.8125);
    private static final VoxelShape NORTH = Shapes.box(0.1875, 0.1875, 0.984365, 0.8125, 0.8125, 0.984375);
    private static final VoxelShape SOUTH = Shapes.box(0.1875, 0.1875, 0.015625, 0.8125, 0.8125, 0.015635);
    private static final VoxelShape WEST = Shapes.box(0.984365, 0.1875, 0.1875, 0.984375, 0.8125, 0.8125);
    private static final VoxelShape EAST = Shapes.box(0.015625, 0.1875, 0.1875, 0.015635, 0.8125, 0.8125);

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
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        var direction = state.getValue(FACING);
        return switch (direction) {
            case UP -> UP;
            case DOWN -> DOWN;
            case EAST -> EAST;
            case WEST -> WEST;
            case SOUTH -> SOUTH;
            default -> NORTH;
        };
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        var direction = pState.getValue(FACING);
        var block = pPos.relative(direction.getOpposite());
        var supportBlockState = pLevel.getBlockState(block);
        return canSupportCenter(pLevel, block, direction);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return !canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var tile = pLevel.getBlockEntity(pPos);
        var inHand = pPlayer.getItemInHand(pHand);
        if (pLevel.isClientSide || !(tile instanceof MarkerBE marker)) {
            return InteractionResult.PASS;
        }
        if (inHand.is(Items.SUGAR)) {
            if (marker.increaseSugarAmount()) {
                if (!pPlayer.isCreative()) {
                    inHand.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        if (inHand.getItem() instanceof DyeItem dye) {
            marker.setColor(dye.getDyeColor());
            if (!pPlayer.isCreative()) {
                inHand.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MarkerBE(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
