package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.item.BaseBlockItem;
import com.matyrobbrt.antsportation.item.BaseItem;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.Utils;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import net.minecraftforge.common.ForgeSpawnEggItem;

@SuppressWarnings("unused")
public class AntsportationItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registry.ITEM_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<Item> ANT_JAR = ITEMS.register("ant_jar",
            () -> new AntJarItem(AntsportationBlocks.ANT_JAR.get(),
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<ForgeSpawnEggItem> ANT_QUEEN_SPAWN_EGG = ITEMS.register("ant_queen_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_QUEEN, 0x290d03, 0x431c11,
                    new Item.Properties().tab(Antsportation.TAB)));

    public static final RegistryObject<ForgeSpawnEggItem> ANT_WORKER_SPAWN_EGG = ITEMS.register("worker_ant_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_WORKER, 0x431c11, 0x290d03,
                    new Item.Properties().tab(Antsportation.TAB)));

    public static final RegistryObject<Item> MARKER = ITEMS.register("marker", () -> new BaseBlockItem(AntsportationBlocks.MARKER.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    static {
        for (final BoxItem.BoxTier tier : BoxItem.BoxTier.values()) {
            tier.registerItem(new Item.Properties().stacksTo(1).rarity(tier.rarity).tab(Antsportation.TAB));
        }
    }

    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", () -> new BaseItem(new Item.Properties().stacksTo(4)
            .tab(Antsportation.TAB).stacksTo(6)) {
        @Override
        public void generateRecipes(DatagenHelper helper) {
            helper.shaped(SPEED_UPGRADE.get())
                    .pattern("RIR")
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('I', Tags.Items.INGOTS_IRON);
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
    });
}
