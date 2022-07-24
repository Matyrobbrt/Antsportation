package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarkerBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = new HashMap<>();
    private static final VoxelShape UP_SHAPE = Shapes.box(0.1875, 0.015625, 0.1875, 0.8125, 0.015635, 0.8125);
    private static final VoxelShape DOWN_SHAPE = Shapes.box(0.1875, 0.984365, 0.1875, 0.8125, 0.984375, 0.8125);
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.1875, 0.1875, 0.984365, 0.8125, 0.8125, 0.984375);
    private static final VoxelShape SOUTH_SHAPE = Shapes.box(0.1875, 0.1875, 0.015625, 0.8125, 0.8125, 0.015635);
    private static final VoxelShape WEST_SHAPE = Shapes.box(0.984365, 0.1875, 0.1875, 0.984375, 0.8125, 0.8125);
    private static final VoxelShape EAST_SHAPE = Shapes.box(0.015625, 0.1875, 0.1875, 0.015635, 0.8125, 0.8125);

    public MarkerBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOL)
                .instabreak()
                .sound(SoundType.SAND)
                .noCollission()
                .color(MaterialColor.COLOR_BROWN));
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
        PROPERTY_BY_DIRECTION.put(Direction.NORTH, NORTH);
        PROPERTY_BY_DIRECTION.put(Direction.SOUTH, SOUTH);
        PROPERTY_BY_DIRECTION.put(Direction.EAST, EAST);
        PROPERTY_BY_DIRECTION.put(Direction.WEST, WEST);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        if (!(pEntity instanceof AntWorkerEntity ant)) {
            return;
        }
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (!(blockEntity instanceof MarkerBE BE)) {
            return;
        }
        if (!pLevel.isClientSide()){
            ant.nodeHistory.add(ant.getNextMarker());
        }
        if (BE.ants.contains(pEntity.getUUID())) {
            return;
        }
        BE.checkMarker(((AntWorkerEntity) pEntity));
        if (BE.nextMarker == null || ant.nodeHistory.contains(BE.nextMarker)) {
            return;
        }
        ant.setNextMarker(BE.nextMarker);
        BE.ants.add(ant.getUUID());
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
        List<BooleanProperty> toggleDirs = new ArrayList<>();
        Arrays.stream(Direction.values())
                .filter(dir -> direction != dir && direction.getOpposite() != dir)
                .forEach(dir -> {
                    var relative = pContext.getClickedPos().relative(dir);
                    var currentBlockColored = coloredCheck(pContext.getLevel(), pContext.getClickedPos());
                    var otherBlockColored = coloredCheck(pContext.getLevel(), relative);
                    if (currentBlockColored != otherBlockColored) {
                        if (connectsTo(relative, pContext.getLevel(), direction)) {
                            var relativeDir = getRelativeDir(direction, dir);
                            var pProperty = PROPERTY_BY_DIRECTION.get(relativeDir);
                            toggleDirs.add(pProperty);
                        }
                    }
                });
        for (var boolProp : toggleDirs) {
            state = state.setValue(boolProp, true);
        }
        if (state.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
            return state;
        }
        return null;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var direction = state.getValue(FACING);
        return switch (direction) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public @NotNull BlockState updateShape(BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
        var currentBlockColored = coloredCheck(pLevel, pCurrentPos);
        var otherBlockColored = coloredCheck(pLevel, pNeighborPos);
        if (pDirection != pState.getValue(FACING) && pDirection != pState.getValue(FACING).getOpposite()) {
            var relativeDir = getRelativeDir(pState.getValue(FACING), pDirection);
            var pValue = false;
            if (pLevel.getBlockState(pNeighborPos).hasProperty(FACING)) {
                pValue = currentBlockColored != otherBlockColored && pState.getValue(FACING) == pLevel.getBlockState(pNeighborPos).getValue(FACING);
            }
            return !canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState.setValue(PROPERTY_BY_DIRECTION.get(relativeDir), pValue);
        }
        return !canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    private boolean coloredCheck(LevelAccessor pLevel, BlockPos blockPos) {
        var blockEntityCurrent = pLevel.getBlockEntity(blockPos);
        var colored = false;
        if (blockEntityCurrent instanceof MarkerBE markerBECurrent) {
            colored = markerBECurrent.isColored();
        }
        return colored;
    }

    public boolean connectsTo(BlockPos pos, BlockGetter level, Direction direction) {
        var tile = level.getBlockEntity(pos);
        return tile instanceof MarkerBE && level.getBlockState(pos).getValue(FACING) == direction;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    public Direction getRelativeDir(Direction markerFacingDir, Direction toTransform) {
        var axis = markerFacingDir.getAxis();
        var axisDirection = markerFacingDir.getAxisDirection();
        return switch (axis) {
            case X -> axisDirection == Direction.AxisDirection.POSITIVE ? toTransform.getCounterClockWise(Direction.Axis.Z).getClockWise() : toTransform.getClockWise(Direction.Axis.Z).getCounterClockWise();
            case Y -> toTransform;
            case Z -> axisDirection == Direction.AxisDirection.POSITIVE ? toTransform.getClockWise(Direction.Axis.X) : toTransform.getCounterClockWise(Direction.Axis.X).getOpposite();
        };
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        var direction = pState.getValue(FACING);
        if(direction == Direction.DOWN) {
            return false;
        }
        var block = pPos.relative(direction.getOpposite());
        var supportBlockState = pLevel.getBlockState(block);
        return canSupportCenter(pLevel, block, direction);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        var tile = pLevel.getBlockEntity(pPos);
        var inHand = pPlayer.getItemInHand(pHand);
        if (pLevel.isClientSide || !(tile instanceof MarkerBE marker)) {
            return InteractionResult.PASS;
        }
        if (inHand.getItem() instanceof DyeItem dye) {
            marker.setColor(dye.getDyeColor());
            shrinkHandItem(pPlayer, inHand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void shrinkHandItem(Player pPlayer, ItemStack inHand) {
        if (!pPlayer.isCreative()) {
            inHand.shrink(1);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new MarkerBE(pPos, pState);
    }
}
