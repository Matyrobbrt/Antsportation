package com.matyrobbrt.antsportation;

import com.matyrobbrt.antsportation.compat.AntsportationCompat;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.network.AntsportationNetwork;
import com.matyrobbrt.antsportation.onetimejoin.OneTimeReward;
import com.matyrobbrt.antsportation.onetimejoin.OneTimeRewardCap;
import com.matyrobbrt.antsportation.onetimejoin.OneTimeRewardListener;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationConfiguredFeatures;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationMenus;
import com.matyrobbrt.antsportation.registration.AntsportationPlacedFeatures;
import com.matyrobbrt.antsportation.registration.AntsportationRecipes;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.config.ClientConfig;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Mod(Antsportation.MOD_ID)
public class Antsportation {
    public static final Rarity ADVANCED = Rarity.create("ADVANCED", ChatFormatting.RED);

    public static final String MOD_ID = "antsportation";
    public static final String MOD_NAME = "Antsportation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public Antsportation() {
        LOGGER.debug("Initialising Antsportation");

        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        AntsportationBlocks.BLOCKS.register(bus);
        AntsportationBlocks.BLOCK_ENTITIES.register(bus);
        AntsportationItems.ITEMS.register(bus);
        AntsportationMenus.MENUS.register(bus);
        AntsportationEntities.ENTITIES.register(bus);
        AntsportationRecipes.TYPES.register(bus);
        AntsportationRecipes.SERIALIZERS.register(bus);
        AntsportationSounds.SOUNDS.register(bus);
        AntsportationConfiguredFeatures.CONFIGURED_FEATURES.register(bus);
        AntsportationPlacedFeatures.PLACED_FEATURES.register(bus);
        OneTimeReward.ONE_TIME_REWARDS.register(bus);

        bus.addListener((final FMLCommonSetupEvent event) -> AntsportationNetwork.register());
        bus.addListener(Antsportation::entityAttributeEvent);
        bus.addListener((final RegisterCapabilitiesEvent event) -> event.register(OneTimeRewardCap.class));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, Antsportation::whenTilled);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MOD_ID + "-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MOD_ID + "-client.toml");

        LOGGER.debug("Antsportation initialized");

        AntsportationCompat.init(bus);
        MinecraftForge.EVENT_BUS.register(OneTimeRewardListener.class);
    }

    private static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(AntsportationEntities.ANT_QUEEN.get(), AntQueenEntity.setAttributes());
        event.put(AntsportationEntities.ANT_SOLDIER.get(), AntSoldierEntity.setAttributes());
        event.put(AntsportationEntities.HILL_ANT_SOLDIER.get(), AntSoldierEntity.setAttributes());
        event.put(AntsportationEntities.ANT_WORKER.get(), AntWorkerEntity.setAttributes());
    }

    public static final CreativeModeTab TAB = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(), MOD_ID) {
        private ItemStack icon;
        @Override
        public @NotNull ItemStack makeIcon() {
            if (icon == null) {
                icon = new ItemStack(AntsportationItems.ANT_JAR.get());
                icon.getOrCreateTagElement("BlockStateTag").putString("antinside", String.valueOf(true));
            }
            return icon;
        }
    };

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String rlStr(String path) {
        return MOD_ID + ":" + path;
    }

    public static void informPlayer(Player player, Component message) {
        player.sendMessage(Translations.MESSAGE_BASE.translate(message), Util.NIL_UUID);
    }

    public static void whenTilled(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated() || event.getContext() == null) {
            return;
        }
        Random random = new Random();
        if (random.nextInt(100) > 95 && (event.getState() != event.getFinalState() || event.getState() != event.getState().getBlock().getToolModifiedState(event.getState(), event.getContext(), ToolActions.HOE_TILL, false))) {
            final var ant = new AntQueenEntity(AntsportationEntities.ANT_QUEEN.get(), event.getContext().getLevel());
            ant.setPos(event.getPos().getX() + 0.5, event.getPos().getY() + 1, event.getPos().getZ() + 0.5);
            event.getContext().getLevel().addFreshEntity(ant);

            for (int i = 0; i < random.nextInt(4); i++) {
                final var soldier = new AntSoldierEntity(AntsportationEntities.ANT_SOLDIER.get(), event.getContext().getLevel());
                final var blockpos = event.getPos().offset(-2 + soldier.getRandom().nextInt(5), 1, -2 + soldier.getRandom().nextInt(5));
                soldier.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                event.getContext().getLevel().addFreshEntity(soldier);
                soldier.aggroAtNearest(Player.class);
            }
        }
    }
}
