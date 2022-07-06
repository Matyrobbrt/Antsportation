package com.matyrobbrt.antsportation.data.loot;

import static com.matyrobbrt.antsportation.registration.AntsportationBlocks.ANTJAR_BLOCK;
import static com.matyrobbrt.antsportation.registration.AntsportationBlocks.BOXER;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;

public class BlockLoot extends net.minecraft.data.loot.BlockLoot {

    @Override
    protected void addTables() {
        dropSelf(BOXER.get());

        dropWhenSilkTouch(ANTJAR_BLOCK.get());
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return List.of();
    }

    @Override
    public void accept(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        this.addTables();
        map.forEach(consumer);
    }
}
