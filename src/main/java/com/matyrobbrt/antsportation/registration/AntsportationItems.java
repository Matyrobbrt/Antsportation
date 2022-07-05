package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.AntJarItem;
import com.matyrobbrt.antsportation.item.BoxItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registry.ITEM_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<Item> ANTJAR_ITEM = ITEMS.register("ant_jar",
            ()->new AntJarItem(AntsportationBlocks.ANTJAR_BLOCK.get(),
                    new Item.Properties().tab(Antsportation.TAB).stacksTo(1)));

    public static final RegistryObject<ForgeSpawnEggItem> ANT_QUEEN_SPAWN_EGG = ITEMS.register("ant_queen_spawn_egg",
            () -> new ForgeSpawnEggItem(AntsportationEntities.ANT_QUEEN,0x290d03,0x431c11 ,
                    new Item.Properties().tab(Antsportation.TAB)));

    static {
        for (final BoxItem.BoxTier tier : BoxItem.BoxTier.values()) {
            tier.registerItem(new Item.Properties().stacksTo(16).rarity(tier.rarity).tab(Antsportation.TAB));
        }
    }
}
