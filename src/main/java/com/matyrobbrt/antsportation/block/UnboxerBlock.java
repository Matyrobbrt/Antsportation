package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.boxing.UnboxerBE;
import com.matyrobbrt.antsportation.compat.jei.JEIInfoProvider;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.data.HasRecipe;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.matyrobbrt.antsportation.block.BoxerBlock.FACING;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class UnboxerBlock extends BaseEntityBlock implements HasRecipe, JEIInfoProvider {
    public UnboxerBlock(Properties props) {
        super(props);
        registerDefaultState(getStateDefinition().any()
                .setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new UnboxerBE(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (pLevel1, pPos, pState1, pBlockEntity) -> ((UnboxerBE) pBlockEntity).tick();
    }

    @Override
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof UnboxerBE boxer && pPlayer instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openGui(serverPlayer, boxer, pPos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof UnboxerBE boxer) {
            boxer.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void generateRecipes(DatagenHelper helper) {
        helper.emptyNBT(this)
                .setEmptyNBTSlots(1, 4)
                .pattern("CBC",
                         "SHS")
                .define('C', Tags.Items.CHESTS)
                .define('B', AntsportationTags.Items.BOXES)
                .define('S', Tags.Items.STONE)
                .define('H', Items.HOPPER);
    }

    @Override
    public @NotNull List<Component> getInfo() {
        return List.of(Translations.JEI_UNBOXER.translate());
    }
}
