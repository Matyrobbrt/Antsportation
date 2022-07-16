package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.AntNestBE;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
public class AntNestBlock extends BaseEntityBlock {
    public static final BooleanProperty PLACEDBYPLAYER = BooleanProperty.create("placedbyplayer");
    public AntNestBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLACEDBYPLAYER, false));
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(PLACEDBYPLAYER, true);
    }

    private static <T extends BlockEntity> void tick(Level pLevel1, BlockPos pPos, BlockState pState1, T pBlockEntity) {
        ((AntNestBE) pBlockEntity).tick();
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PLACEDBYPLAYER);
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
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof AntNestBE antNest) {
            antNest.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @javax.annotation.Nullable BlockEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
        if (!pLevel.isClientSide && pTe instanceof AntNestBE antNest && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
            if (antNest.hasQueen || pState.getValue(PLACEDBYPLAYER)) {
                for (int i = 0; i < 5; i++) {
                    if (RANDOM.nextBoolean()) {
                        AntSoldierEntity entity = new AntSoldierEntity(AntsportationEntities.ANT_SOLDIER.get(), pLevel);
                        entity.setPos(pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5);
                        pLevel.addFreshEntity(entity);
                    }
                }
            }
        }

    }
}
