package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.item.BaseBlockItem;
import com.matyrobbrt.antsportation.item.BaseItem;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("unused")
public class AntsportationItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Antsportation.MOD_ID);

    public static final RegistryObject<AntJarItem> ANT_JAR = ITEMS.register("ant_jar",
            () -> new AntJarItem(AntsportationBlocks.ANT_JAR.get(),
                    new Item.Properties().stacksTo(1)) {

                @Override
                public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
                    return ANT_JAR.get().getDefaultInstance();
                }
            });

    public static final RegistryObject<ForgeSpawnEggItem> ANT_QUEEN_SPAWN_EGG = ITEMS.register("ant_queen_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_QUEEN, 0x290d03, 0x431c11,
                    new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> ANT_SOLDIER_SPAWN_EGG = ITEMS.register("soldier_ant_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_SOLDIER, 0x431c11, 0x290d03,
                    new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> ANT_WORKER_SPAWN_EGG = ITEMS.register("ant_worker_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_WORKER, 0x431c04, 0x290d03,
                    new Item.Properties()));

    public static final RegistryObject<Item> MARKER = ITEMS.register("marker", () -> new BaseBlockItem(AntsportationBlocks.MARKER.get(),
            new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CHUNK_LOADING_MARKER = ITEMS.register("chunk_loading_marker", () -> new BaseBlockItem(AntsportationBlocks.CHUNK_LOADING_MARKER,
            new Item.Properties().rarity(Rarity.RARE)) {
        @Override
        @ParametersAreNonnullByDefault
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            if (!ServerConfig.CONFIG.ants().chunkLoadingMarkers().get()) {
                pTooltip.add(Translations.CHUNK_LOADING_DISABLED.translate().withStyle(ChatFormatting.RED));
            }
        }

        @Override
        public InteractionResult place(BlockPlaceContext p_40577_) {
            if (!ServerConfig.CONFIG.ants().chunkLoadingMarkers().get()) {
                return InteractionResult.FAIL;
            }
            return super.place(p_40577_);
        }

        @Override
        public boolean shouldFillTab() {
            return ServerConfig.CONFIG.ants().chunkLoadingMarkers().get();
        }
    });

    static {
        for (final BoxItem.BoxTier tier : BoxItem.BoxTier.values()) {
            tier.registerItem(new Item.Properties().stacksTo(1).rarity(tier.rarity));
        }
    }

    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", () -> new BaseItem(new Item.Properties().stacksTo(4)
            .stacksTo(6)) {
        @Override
        public void generateRecipes(DatagenHelper helper) {
            helper.shapeless(this)
                    .requires(Items.SUGAR, 1)
                    .requires(Tags.Items.DUSTS_REDSTONE, 1)
                    .requires(Tags.Items.INGOTS_IRON, 1)
                    .requires(Items.PAPER, 1);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            pTooltipComponents.add(Translations.SPEED_UPGRADE_TOOLTIP.translate().withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Translations.SPEED_UPGRADE_TOOLTIP2.translate(
                    Utils.textComponent(ServerConfig.getBoxing(ServerConfig.Boxing::upgradeReduction)).withStyle(ChatFormatting.AQUA)
            ));
            pTooltipComponents.add(Translations.SPEED_UPGRADE_TOOLTIP3.translate(
                    Utils.textComponent(ServerConfig.getBoxing(ServerConfig.Boxing::upgradeEnergyUsage)).withStyle(ChatFormatting.GREEN)
            ));
        }

        @Override
        public @NotNull List<Component> getInfo() {
            return List.of(Translations.JEI_SPEED_UPGRADE.translate());
        }
    });
}
