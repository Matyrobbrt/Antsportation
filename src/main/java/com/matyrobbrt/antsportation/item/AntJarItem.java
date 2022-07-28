package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@SuppressWarnings("SpellCheckingInspection")
public class AntJarItem extends BaseBlockItem {

    public AntJarItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public static boolean hasAntInside(ItemStack itemStack) {
        if (itemStack.getItem() instanceof AntJarItem) {
            return itemStack.getTag() != null && Boolean.parseBoolean(itemStack.getTag().getCompound("BlockStateTag").getString("antinside"));
        } else {
            return false;
        }
    }

    public static ItemStack withAnt() {
        final var stack = new ItemStack(AntsportationItems.ANT_JAR.get());
        stack.getOrCreateTagElement("BlockStateTag").putString("antinside", String.valueOf(true));
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof AntQueenEntity) {
            ItemStack itemStack = pStack.copy();
            itemStack.getOrCreateTag();
            if (itemStack.getTag() != null && !itemStack.getTag().contains("BlockStateTag")) {
                itemStack.getTag().put("BlockStateTag", new CompoundTag());
            }
            if (!Boolean.parseBoolean(itemStack.getTag().getCompound("BlockStateTag").getString("antinside"))) {
                itemStack.getTag().getCompound("BlockStateTag").putString("antinside", String.valueOf(true));
                pPlayer.setItemInHand(pUsedHand, itemStack);
                pInteractionTarget.remove(Entity.RemovalReason.DISCARDED);
                return InteractionResult.sidedSuccess(pPlayer.level.isClientSide());
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        Level level = itemEntity.getLevel();
        if (!level.isClientSide() && hasAntInside(itemEntity.getItem())){
            AntQueenEntity entity = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), level);
            entity.setPos(itemEntity.position());
            level.addFreshEntity(entity);
        }
    }

    @Override
    public void generateRecipes(DatagenHelper helper) {
        helper.shaped(this)
                .pattern(
                        "WWW",
                        "G G",
                        "GGG"
                )
                .define('W', ItemTags.PLANKS)
                .define('G', Tags.Items.GLASS);
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
            if (usedItemTag != null && Boolean.parseBoolean(usedItemTag.getCompound("BlockStateTag").getString("antinside"))) {
                if (!pContext.getLevel().isClientSide()) {
                    LivingEntity entityToSpawn = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), level);
                    entityToSpawn.setPos(pContext.getClickLocation());
                    level.addFreshEntity(entityToSpawn);
                }
                usedItemTag.getCompound("BlockStateTag").putString("antinside", String.valueOf(false));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
        if (this.allowdedIn(pGroup)) {
            this.getBlock().fillItemCategory(pGroup, pItems);
            final var withAnt = getDefaultInstance();
            withAnt.getOrCreateTagElement("BlockStateTag").putString("antinside", String.valueOf(true));
            pItems.add(withAnt);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if (hasAntInside(pStack)) {
            pTooltip.add(Translations.HAS_ANT_TOOLTIP.translate().withStyle(ChatFormatting.GOLD));
        }
    }
}
