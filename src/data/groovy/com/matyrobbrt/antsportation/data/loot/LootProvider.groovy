package com.matyrobbrt.antsportation.data.loot

import com.matyrobbrt.antsportation.data.advancement.AdvancementGenerator
import com.mojang.datafixers.util.Pair
import groovy.transform.CompileDynamic
import net.minecraft.data.DataGenerator
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.LootTables
import net.minecraft.world.level.storage.loot.ValidationContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

import javax.annotation.Nonnull
import javax.annotation.ParametersAreNonnullByDefault
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class LootProvider extends LootTableProvider {
    private final List subProviders

    @SuppressWarnings('GroovyAssignabilityCheck')
    @CompileDynamic
    LootProvider(DataGenerator pGenerator, AdvancementGenerator generator) {
        super(pGenerator)
        subProviders = List.of(
                Pair.of(makeSupplier(EntityLoot.&new), LootContextParamSets.ENTITY),
                Pair.of(makeSupplier(BlockLoot.&new), LootContextParamSets.BLOCK),
                Pair.of(new Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>() {
                    @Override
                    Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> get() {
                        return new AdvancementLoot(generator)
                    }
                }, LootContextParamSets.ADVANCEMENT_REWARD)
        )
    }

    static <T> Supplier<T> makeSupplier(Closure<T> clos) {
        return new Supplier<T>() {
            @Override
            T get() {
                return clos.call()
            }
        }
    }

    @Nonnull
    @Override
    protected List getTables() {
        return subProviders
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) {
        map.forEach((location, table) -> LootTables.validate(tracker, location, table))
    }
}