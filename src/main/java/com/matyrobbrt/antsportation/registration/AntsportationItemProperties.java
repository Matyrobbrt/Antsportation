package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.item.AntJarItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class AntsportationItemProperties {
    public static void addCustomItemProperties(){
        makeAntJar(AntsportationItems.ANTJAR_ITEM.get());
    }

    private static void makeAntJar(Item item){
        ItemProperties.register(item, new ResourceLocation("filled"), (stack, level, entity, seed)-> (AntJarItem.hasAntInside(stack)) ? 1 : 0);
    }
}
