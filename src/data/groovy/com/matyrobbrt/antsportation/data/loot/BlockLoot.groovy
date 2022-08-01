package com.matyrobbrt.antsportation.data.loot

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import org.jetbrains.annotations.NotNull

import javax.annotation.Nonnull
import java.util.function.BiConsumer
import java.util.function.Supplier

import static com.matyrobbrt.antsportation.registration.AntsportationBlocks.*

class BlockLoot extends net.minecraft.data.loot.BlockLoot {

    @Override
    protected void addTables() {
        dropSelf(BOXER)
        dropSelf(UNBOXER)
        dropSelf(ANT_NEST)
        dropSelf(ANT_HILL)

        dropWhenSilkTouch(ANT_JAR.get())

        dropSelf(MARKER)
        dropSelf(CHUNK_LOADING_MARKER)
    }

    private void dropSelf(Supplier<? extends Block> block) {
        dropSelf(block.get())
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return List.of()
    }

    @Override
    void accept(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.addTables()
        map.forEach(consumer)
    }
}
