package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.BoxerBE;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.data.HasRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoxerBlock extends BaseEntityBlock implements HasRecipe {
    public BoxerBlock(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BoxerBE(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (pLevel1, pPos, pState1, pBlockEntity) -> ((BoxerBE) pBlockEntity).tick();
    }

    @Override
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof BoxerBE boxer && pPlayer instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openGui(serverPlayer, boxer, pPos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof BoxerBE boxer) {
            boxer.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void generateRecipes(DatagenHelper helper) {
        helper.emptyNBT(this)
                .setEmptyNBTSlots(3, 4, 5)
                .pattern("C", "B", "C")
                .define('C', Tags.Items.CHESTS)
                .define('B', TagKey.create(Registry.ITEM_REGISTRY, Antsportation.rl("boxes")));
    }
}
