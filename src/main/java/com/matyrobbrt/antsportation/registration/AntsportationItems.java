package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

public class AntsportationItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registry.ITEM_REGISTRY, Antsportation.MOD_ID);

    static {
        for (final BoxItem.BoxTier tier : BoxItem.BoxTier.values()) {
            tier.registerItem(new Item.Properties().stacksTo(16).rarity(tier.rarity).tab(Antsportation.TAB));
        }
    }
}
