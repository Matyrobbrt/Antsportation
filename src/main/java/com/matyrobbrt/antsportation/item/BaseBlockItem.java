package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.data.HasRecipe;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class BaseBlockItem extends BlockItem implements HasRecipe {
    public BaseBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties.tab(Antsportation.TAB));
    }
}
