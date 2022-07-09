package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.block.entity.AntHillBE;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class AntHillBlock extends BaseEntityBlock {
    public AntHillBlock(Properties p_49224_) {
        super(p_49224_);
    }

    private static <T extends BlockEntity> void tick(Level pLevel1, BlockPos pPos, BlockState pState1, T pBlockEntity) {
        ((AntHillBE) pBlockEntity).tick();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new AntHillBE(pPos, pState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof AntHillBE antHill){
            if(AntJarItem.hasAntInside(pPlayer.getItemInHand(pHand))) {
                if (!antHill.hasQueen) {
                    antHill.hasQueen = true;
                    pPlayer.setItemInHand(pHand, new ItemStack(AntsportationItems.ANT_JAR.get()));
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.FAIL;
                }
            } else if(!AntJarItem.hasAntInside(pPlayer.getItemInHand(pHand))){
                if(antHill.hasQueen){
                    antHill.hasQueen = false;
                    CompoundTag withAnt = new CompoundTag();
                    withAnt.put("BlockStateTag", new CompoundTag());
                    withAnt.getCompound("BlockStateTag").putString("antinside", "true");
                    ItemStack antJar = new ItemStack(AntsportationItems.ANT_JAR.get());
                    antJar.setTag(withAnt);
                    pPlayer.setItemInHand(pHand, antJar);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : AntHillBlock::tick;
    }

    @Override
    public void onRemove(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof AntHillBE antHill) {
            antHill.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
