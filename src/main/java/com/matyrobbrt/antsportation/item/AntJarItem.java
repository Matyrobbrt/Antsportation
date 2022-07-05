package com.matyrobbrt.antsportation.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class AntJarItem extends BlockItem {
    public AntJarItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public static boolean hasAntInside(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().getCompound("BlockStateTag").getString("antinside").matches("true");
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        //TODO: Change the entity to Ant queen.
        if (pInteractionTarget instanceof Silverfish) {
            ItemStack itemStack = pStack.copy();
            itemStack.getOrCreateTag();
            if(itemStack.getTag() != null && !itemStack.getTag().contains("BlockStateTag")){
                itemStack.getTag().put("BlockStateTag", new CompoundTag());
            }
            itemStack.getTag().getCompound("BlockStateTag").putString("antinside", "true");
            pPlayer.setItemInHand(pUsedHand,itemStack);
            pInteractionTarget.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.sidedSuccess(pPlayer.level.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }



    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player playerUsed = pContext.getPlayer();
        Level level = pContext.getLevel();
        CompoundTag usedItemTag = pContext.getItemInHand().getTag();

        if (playerUsed == null) {
            return InteractionResult.FAIL;
        } else if (playerUsed.isCrouching()) {
            return this.place(new BlockPlaceContext(pContext));
        } else {
            if (usedItemTag != null && usedItemTag.getCompound("BlockStateTag").getString("antinside").matches("true")) {
                //TODO: Change the entity to Ant queen.
                if (!pContext.getLevel().isClientSide()) {
                    LivingEntity entityToSpawn = new Silverfish(EntityType.SILVERFISH, level);
                    entityToSpawn.setPos(pContext.getClickLocation());
                    level.addFreshEntity(entityToSpawn);
                }
                usedItemTag.getCompound("BlockStateTag").putString("antinside", "false");
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
