package com.matyrobbrt.antsportation.data.loot

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
import java.util.function.Supplier

class LootProvider extends LootTableProvider {
    private final List subProviders
    @CompileDynamic
    LootProvider(DataGenerator pGenerator) {
        super(pGenerator)
        subProviders = List.of(
                Pair.of(makeSupplier(EntityLoot.&new), LootContextParamSets.ENTITY),
                Pair.of(makeSupplier(BlockLoot.&new), LootContextParamSets.BLOCK)
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