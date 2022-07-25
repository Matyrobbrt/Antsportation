package com.matyrobbrt.antsportation.util.config;

import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationTags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.Map;
import java.util.function.Function;

public record ServerConfig(Boxing boxing, Ants ants) {
    public static final ServerConfig CONFIG;
    public static final ForgeConfigSpec SPEC;

    public static final Map<String, ForgeConfigSpec.ConfigValue<?>> BY_PATH;

    static {
        final var builder = new StoringConfigBuilder();

        builder.comment("Configuration for boxing / unboxing", "Note: boxing machines refers to boxers / unboxers")
                .push("boxing");
        final Boxing boxing;
        {
            boxing = new Boxing(
                    builder.comment("If boxing machines should use energy (Forge Energy)")
                            .define("useEnergy", true),
                    builder.comment("The amount of energy boxing machines can store.")
                            .defineInRange("energyCapacity", 100_000, 1, Integer.MAX_VALUE),
                    builder.comment("How many ticks each speed upgrade will reduce from the needed progress of boxing machines.")
                            .defineInRange("upgradeReduction", 15, 1, Integer.MAX_VALUE),
                    builder.comment("How much speed upgrades will increase the energy usage of boxing machines.")
                            .defineInRange("upgradeEnergyUsage", 20, 1, Integer.MAX_VALUE),
                    builder.comment("How much ticks boxing machines will need per process.")
                            .defineInRange("baseNeededTicks", 100, 1, Integer.MAX_VALUE),
                    builder.comment("How much energy boxing machines will use per tick, by default.")
                            .defineInRange("baseUsedEnergy", 50, 1, Integer.MAX_VALUE)
            );
        }
        builder.pop();

        builder.comment("Configuration for ants.")
                .push("ants");
        final Ants ants;
        {
            ants = new Ants(
                    builder.comment("The delay in ticks until Ant Hills will spawn a new Worker Ant.")
                            .defineInRange("hillSpawnDelay", 100, 1, 100_000),
                    builder.comment("The delay in ticks before an Ant Nest will attempt to push / pull into a hill.")
                            .defineInRange("nestIORate", 5, 1, 100_000),
                    builder.comment("If the only items that can be transported directly by ants are the ones in the '" + AntsportationTags.Items.ANT_TRANSPORTABLE.location() + "' tag.",
                                    "If 'false', all items can be transported directly by ants.")
                            .define("onlyTransportableItems", true)
            );
        }
        builder.pop();

        SPEC = builder.build();

        CONFIG = new ServerConfig(boxing, ants);
        BY_PATH = builder.getByPath();
    }

    public record Boxing(ForgeConfigSpec.BooleanValue useEnergy, IntValue energyCapacity, IntValue upgradeReduction, IntValue upgradeEnergyUsage,
                         IntValue baseNeededTicks, IntValue baseUsedEnergy) {
        public int getIORate() {
            return (int) ((AntsportationItems.SPEED_UPGRADE.get().getDefaultInstance().getMaxStackSize() * upgradeEnergyUsage.get()) * 1.50);
        }
    }
    public record Ants(IntValue hillSummonRate, IntValue nestIORate, ForgeConfigSpec.BooleanValue onlyTransportableItems) {}

    public static int getBoxing(Function<Boxing, IntValue> getter) {
        return getter.apply(CONFIG.boxing()).get();
    }
}
