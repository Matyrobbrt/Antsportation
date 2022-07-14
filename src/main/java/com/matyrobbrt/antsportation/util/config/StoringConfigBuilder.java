package com.matyrobbrt.antsportation.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StoringConfigBuilder extends ForgeConfigSpec.Builder {
    private final Map<String, ForgeConfigSpec.ConfigValue<?>> byPath = new HashMap<>();

    @Override
    public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, ForgeConfigSpec.ValueSpec value, Supplier<T> defaultSupplier) {
        final var val = super.define(path, value, defaultSupplier);
        byPath.put(String.join(".", path), val);
        return val;
    }

    public Map<String, ForgeConfigSpec.ConfigValue<?>> getByPath() {
        return Map.copyOf(byPath);
    }
}
