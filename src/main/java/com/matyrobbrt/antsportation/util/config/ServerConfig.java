package com.matyrobbrt.antsportation.util.config;

import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record ServerConfig(Boxing boxing) {
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

        SPEC = builder.build();

        CONFIG = new ServerConfig(boxing);
        BY_PATH = builder.getByPath();
    }
    public record Boxing(ForgeConfigSpec.BooleanValue useEnergy, IntValue energyCapacity, IntValue upgradeReduction, IntValue upgradeEnergyUsage,
                         IntValue baseNeededTicks, IntValue baseUsedEnergy) {
        public int getIORate() {
            return (int) ((AntsportationItems.SPEED_UPGRADE.get().getDefaultInstance().getMaxStackSize() * upgradeEnergyUsage.get()) * 1.50);
        }
    }

    public static int getBoxing(Function<Boxing, IntValue> getter) {
        return getter.apply(CONFIG.boxing()).get();
    }
}
