package com.matyrobbrt.antsportation.data.advancement

import net.minecraft.advancements.Advancement
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

import java.util.function.BiConsumer
import java.util.function.Consumer

interface AdvancementProvider {
    void register(Consumer<Advancement> creator)

    default void generateRewards(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {}
}