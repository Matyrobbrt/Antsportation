package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.AntJarBE;
import com.matyrobbrt.antsportation.compat.jei.JEIInfoProvider;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AntJarBlock extends Block implements EntityBlock, JEIInfoProvider {

    public static final BooleanProperty ANTINSIDE = BooleanProperty.create("antinside");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AntJarBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(ANTINSIDE, false).setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(ANTINSIDE, false).setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ANTINSIDE, FACING);
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide() && pState.getValue(ANTINSIDE)) {
            AntQueenEntity entity = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), pLevel);
            entity.setPos(pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5);
            pLevel.addFreshEntity(entity);
        }
    }

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(5.399999999999997, 0, 5.3999999999999995, 5.899999999999997, 7, 10.6),
            Block.box(10.100000000000001, 0, 5.4, 10.600000000000001, 7, 10.6),
            Block.box(5.9, 0, 10.160000000000004, 10.1, 7, 10.600000000000003),
            Block.box(5.9, 0, 5.399999999999997, 10.1, 7, 5.839999999999996),
            Block.box(5.9, 0, 5.8, 10.1, 0.09999999999999998, 10.2),
            Block.box(5.7, 7, 5.7, 10.3, 8, 10.3)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AntJarBE(blockPos, blockState);
    }

    @Override
    public List<Component> getInfo() {
        return List.of(
                Translations.JEI_ANT_JAR.translate()
        );
    }
}
