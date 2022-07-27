package com.matyrobbrt.antsportation.data.loot

import com.matyrobbrt.antsportation.data.advancement.AdvancementGenerator
import groovy.transform.Canonical
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

import java.util.function.BiConsumer
import java.util.function.Consumer

@Canonical
class AdvancementLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    AdvancementGenerator advancementGenerator

    @Override
    void accept(BiConsumer<ResourceLocation, LootTable.Builder> resourceLocationBuilderBiConsumer) {
        advancementGenerator.providers.each {it.generateRewards(resourceLocationBuilderBiConsumer)}
    }
}
