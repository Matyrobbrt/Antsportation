package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

@SuppressWarnings("SameParameterValue")
public class AntsportationTags {
    public static final class Items {
        public static final TagKey<Item> BOXES = grabMod("boxes");
        public static final TagKey<Item> ANT_TRANSPORTABLE = grabMod("ant_transportable");

        private static TagKey<Item> grabMod(String path) {
            return TagKey.create(Registries.ITEM, Antsportation.rl(path));
        }
    }
    public static final class Entities {
        public static final TagKey<EntityType<?>> ANTS = grabMod("ants");

        private static TagKey<EntityType<?>> grabMod(String path) {
            return TagKey.create(Registries.ENTITY_TYPE, Antsportation.rl(path));
        }
    }
}
