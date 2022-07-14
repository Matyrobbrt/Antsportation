package com.matyrobbrt.antsportation;

import com.matyrobbrt.antsportation.compat.patchouli.PatchouliCompat;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.network.AntsportationNetwork;
import com.matyrobbrt.antsportation.registration.*;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Antsportation.MOD_ID)
public class Antsportation {
    public static final Rarity ADVANCED = Rarity.create("ADVANCED", ChatFormatting.RED);

    public static final String MOD_ID = "antsportation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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

        bus.addListener((final FMLCommonSetupEvent event) -> AntsportationNetwork.register());
        bus.addListener(Antsportation::entityAttributeEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MOD_ID + "-server.toml");

        LOGGER.debug("Antsportation initialized");

        if (ModList.get().isLoaded("patchouli") && FMLEnvironment.dist == Dist.CLIENT) {
            bus.register(PatchouliCompat.class);
            LOGGER.debug("Initialised Patchouli compat");
        }
    }

    private static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(AntsportationEntities.ANT_QUEEN.get(), AntQueenEntity.setAttributes());
        event.put(AntsportationEntities.ANT_SOLDIER.get(), AntSoldierEntity.setAttributes());
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
}
