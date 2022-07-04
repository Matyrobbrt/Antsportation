package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import net.minecraft.world.item.Item;

public abstract class BaseItem extends Item {

    public BaseItem(Properties pProperties) {
        super(pProperties.tab(Antsportation.TAB));
    }

    public void generateRecipes(DatagenHelper helper) {

    }
}
