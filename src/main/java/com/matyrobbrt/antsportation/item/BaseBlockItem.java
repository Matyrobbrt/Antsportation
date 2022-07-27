package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.compat.jei.JEIInfoProvider;
import com.matyrobbrt.antsportation.data.HasRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class BaseBlockItem extends BlockItem implements HasRecipe, JEIInfoProvider {
    public BaseBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties.tab(Antsportation.TAB));
    }
    public BaseBlockItem(Supplier<? extends Block> pBlock, Properties pProperties) {
        this(pBlock.get(), pProperties);
    }

    @Override
    public @NotNull List<Component> getInfo() {
        return List.of();
    }
}
