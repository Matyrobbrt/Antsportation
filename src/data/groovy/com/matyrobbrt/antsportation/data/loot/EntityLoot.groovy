package com.matyrobbrt.antsportation.data.loot


import net.minecraft.core.Registry

import javax.annotation.Nonnull

class EntityLoot extends net.minecraft.data.loot.EntityLoot {
    @Override
    protected void addTables() {
    }

    @Nonnull
    @Override
    protected Iterable getKnownEntities() {
        map.keySet().collect(Registry.ENTITY_TYPE.&get)
    }
}
