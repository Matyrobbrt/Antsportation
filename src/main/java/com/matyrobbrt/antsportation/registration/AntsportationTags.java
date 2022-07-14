package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@SuppressWarnings("SameParameterValue")
public class AntsportationTags {
    public static final class Items {
        public static final TagKey<Item> BOXES = grabMod("boxes");

        private static TagKey<Item> grabMod(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, Antsportation.rl(path));
        }
    }
}
