package com.matyrobbrt.antsportation.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StoringConfigBuilder extends ForgeConfigSpec.Builder {
    private final Map<String, ForgeConfigSpec.ConfigValue<?>> byPath = new HashMap<>();

    private final List<String> curPath = new ArrayList<>();

    @Override
    public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, ForgeConfigSpec.ValueSpec value, Supplier<T> defaultSupplier) {
        final var val = super.define(path, value, defaultSupplier);
        final var curPathStr = String.join(".", curPath);
        byPath.put(curPathStr.isEmpty() ? "" : curPathStr + "." + String.join(".", path), val);
        return val;
    }

    @Override
    public ForgeConfigSpec.Builder push(List<String> path) {
        curPath.addAll(path);
        return super.push(path);
    }

    @Override
    public ForgeConfigSpec.Builder pop(int count) {
        for (int x = 0; x < count; x++)
            curPath.remove(curPath.size() - 1);
        return super.pop(count);
    }

    public Map<String, ForgeConfigSpec.ConfigValue<?>> getByPath() {
        return Map.copyOf(byPath);
    }
}
