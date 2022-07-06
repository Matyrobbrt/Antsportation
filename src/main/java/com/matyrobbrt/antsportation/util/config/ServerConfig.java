package com.matyrobbrt.antsportation.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.function.Function;

public record ServerConfig(Boxing boxing) {
    public static final ServerConfig CONFIG;
    public static final ForgeConfigSpec SPEC;
    static {
        final var builder = new ForgeConfigSpec.Builder();
        builder.comment("Configuration for boxing / unboxing")
                .push("boxing");
        final Boxing boxing;
        {
            boxing = new Boxing(
                    builder.comment("If boxing / unboxing machine should use energy (Forge Energy)")
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
    }
    public record Boxing(ForgeConfigSpec.BooleanValue useEnergy, IntValue energyCapacity, IntValue upgradeReduction, IntValue upgradeEnergyUsage,
                         IntValue baseNeededTicks, IntValue baseUsedEnergy) {}

    public static int getBoxing(Function<Boxing, IntValue> getter) {
        return getter.apply(CONFIG.boxing()).get();
    }
}
