package com.matyrobbrt.antsportation.data.loot;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nonnull;

public class EntityLoot extends net.minecraft.data.loot.EntityLoot {
    @Override
    protected void addTables() {
    }

    @Override
    @Nonnull
    protected Iterable<EntityType<?>> getKnownEntities() {
        return map.keySet().stream().<EntityType<?>>map(Registry.ENTITY_TYPE::get).distinct().toList();
    }
}
