package com.matyrobbrt.antsportation.data.server

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationTags
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.BlockTagsProvider
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import org.jetbrains.annotations.Nullable

class Tags {
    static class Blocks extends BlockTagsProvider {

        Blocks(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Antsportation.MOD_ID, existingFileHelper)
        }

        @Override
        protected void addTags() {
            AntsportationBlocks.MINE_DATA.forEach((block, data) -> {
                tag(data.mineable().tag).add(block.get())
                tag(data.toolTier().tag).add(block.get())
            })
        }
    }

    static class Items extends ItemTagsProvider {

        Items(DataGenerator pGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, blockTagsProvider, Antsportation.MOD_ID, existingFileHelper)
        }

        @Override
        protected void addTags() {
            final var boxes = tag(AntsportationTags.Items.BOXES)
            for (final value in BoxItem.BoxTier.values()) {
                boxes.add(value.asItem())
            }

            tag(AntsportationTags.Items.ANT_TRANSPORTABLE)
                    .addTag(AntsportationTags.Items.BOXES)
        }
    }
}
