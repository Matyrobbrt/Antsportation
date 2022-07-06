package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class Tags {
    public static class Blocks extends BlockTagsProvider {

        public Blocks(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Antsportation.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            AntsportationBlocks.MINE_DATA.forEach((block, data) -> {
                tag(data.mineable().tag).add(block.get());
                tag(data.toolTier().tag).add(block.get());
            });
        }
    }

    public static class Items extends ItemTagsProvider {

        public Items(DataGenerator pGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, blockTagsProvider, Antsportation.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            final var boxes = tag(TagKey.create(Registry.ITEM_REGISTRY, Antsportation.rl("boxes")));
            for (BoxItem.BoxTier value : BoxItem.BoxTier.values()) {
                boxes.add(value.asItem());
            }
        }
    }
}
