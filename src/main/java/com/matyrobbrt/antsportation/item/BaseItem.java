package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.HasRecipe;
import net.minecraft.world.item.Item;

public abstract class BaseItem extends Item implements HasRecipe {

    public BaseItem(Properties pProperties) {
        super(pProperties.tab(Antsportation.TAB));
    }
}
