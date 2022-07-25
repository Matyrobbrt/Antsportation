package com.matyrobbrt.antsportation.util.config;

import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.Map;
import java.util.function.Function;

public record ClientConfig(ForgeConfigSpec.BooleanValue showTransportableItems) {
    public static final ClientConfig CONFIG;
    public static final ForgeConfigSpec SPEC;

    public static final Map<String, ForgeConfigSpec.ConfigValue<?>> BY_PATH;

    static {
        final var builder = new StoringConfigBuilder();

        final var showTransportableItems = builder.comment("If ant transportable items should have a tooltip indicating that.")
                .define("showTransportableItems", true);

        SPEC = builder.build();

        CONFIG = new ClientConfig(showTransportableItems);
        BY_PATH = builder.getByPath();
    }

}
