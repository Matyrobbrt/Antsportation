package com.matyrobbrt.antsportation.data.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.matyrobbrt.antsportation.registration.AntsportationBlocks.*;

public class BlockLoot extends net.minecraft.data.loot.BlockLoot {

    @Override
    protected void addTables() {
        dropSelf(BOXER);
        dropSelf(ANT_NEST);

        dropWhenSilkTouch(ANT_JAR.get());
    }

    private void dropSelf(Supplier<? extends Block> block) {
        dropSelf(block.get());
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
