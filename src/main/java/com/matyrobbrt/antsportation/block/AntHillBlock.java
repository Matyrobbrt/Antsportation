package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.AntHillBE;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class AntHillBlock extends BaseEntityBlock {
    public static final BooleanProperty PLACEDBYPLAYER = BooleanProperty.create("placedbyplayer");
    public static final BooleanProperty IS_GRASSY = BooleanProperty.create("grassy");
    public AntHillBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PLACEDBYPLAYER, false)
                .setValue(IS_GRASSY, false));
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(PLACEDBYPLAYER, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PLACEDBYPLAYER, IS_GRASSY);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new AntHillBE(pPos, pState);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide() && pEntity instanceof AntWorkerEntity ant && pLevel.getBlockEntity(pPos) instanceof AntHillBE blockEntity && !blockEntity.hasQueen) {
            final var remainder = blockEntity.addItem(ant.getOffhandItem());
            ant.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            if (!remainder.isEmpty()) {
                ant.spawnAtLocation(remainder);
            }
            ant.discard();
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof AntHillBE antHill) {
            final var inHand = pPlayer.getItemInHand(pHand);
            if (AntJarItem.hasAntInside(inHand)) {
                if (!antHill.hasQueen) {
                    antHill.hasQueen = true;
                    pPlayer.setItemInHand(pHand, new ItemStack(AntsportationItems.ANT_JAR.get()));
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else if (!inHand.isEmpty() && inHand.getItem() instanceof AntJarItem) {
                if (antHill.hasQueen) {
                    antHill.hasQueen = false;
                    CompoundTag withAnt = new CompoundTag();
                    {
                        final CompoundTag comp = new CompoundTag();
                        comp.putString("antinside", "true");
                        withAnt.put("BlockStateTag", comp);
                    }
                    ItemStack antJar = new ItemStack(AntsportationItems.ANT_JAR.get());
                    antJar.setTag(withAnt);
                    pPlayer.setItemInHand(pHand, antJar);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }
            pState.setValue(IS_GRASSY, !antHill.hasQueen);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 11, 5, 11, 12, 11),
            Block.box(0, 0, 0, 16, 5, 16),
            Block.box(1, 5, 1, 15, 7, 15),
            Block.box(2, 7, 2, 14, 9, 14),
            Block.box(3, 9, 3, 13, 10, 13),
            Block.box(4, 10, 4, 12, 11, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (pLevel1, pPos, pState1, pBlockEntity) -> ((AntHillBE) pBlockEntity).tick();
    }

    @Override
    public void onRemove(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof AntHillBE antHill) {
            antHill.dropContents();
            if (!pLevel.isClientSide()) {
                for (int i = 0; i < 3; i++) {
                    if (RANDOM.nextBoolean() && (antHill.hasQueen || !pState.getValue(PLACEDBYPLAYER))) {
                        AntSoldierEntity entity = new AntSoldierEntity(AntsportationEntities.ANT_SOLDIER.get(), pLevel);
                        entity.setPos(pPos.getX(), pPos.getY(), pPos.getZ());
                        pLevel.addFreshEntity(entity);
                    }
                }
                if (!pState.getValue(PLACEDBYPLAYER) || antHill.hasQueen) {
                    AntQueenEntity entity = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), pLevel);
                    entity.setPos(pPos.getX()+0.5, pPos.getY(), pPos.getZ()+0.5);
                    pLevel.addFreshEntity(entity);
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
