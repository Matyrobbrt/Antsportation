package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.data.ShapedRecipe;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("SpellCheckingInspection")
public class AntJarItem extends BaseBlockItem {
    private final Consumer<ShapedRecipe> recipe;

    public AntJarItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        recipe = recipe -> recipe.pattern(
                "WWW",
                "GBG",
                "GGG"
                )
                .define('W', ItemTags.PLANKS)
                .define('G', Tags.Items.GLASS)
                .define('B', Items.GLASS_BOTTLE);
    }

    public static boolean hasAntInside(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().getCompound("BlockStateTag").getString("antinside").matches("true");
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof AntQueenEntity) {
            ItemStack itemStack = pStack.copy();
            itemStack.getOrCreateTag();
            if (itemStack.getTag() != null && !itemStack.getTag().contains("BlockStateTag")) {
                itemStack.getTag().put("BlockStateTag", new CompoundTag());
            }
            if(itemStack.getTag().getCompound("BlockStateTag").getString("antinside").matches("false")){
                itemStack.getTag().getCompound("BlockStateTag").putString("antinside", "true");
                pPlayer.setItemInHand(pUsedHand, itemStack);
                pInteractionTarget.remove(Entity.RemovalReason.DISCARDED);
                return InteractionResult.sidedSuccess(pPlayer.level.isClientSide());
            }else{
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void generateRecipes(DatagenHelper helper) {
        recipe.accept(helper.shaped(this));
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
                if (!pContext.getLevel().isClientSide()) {
                    LivingEntity entityToSpawn = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), level);
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
