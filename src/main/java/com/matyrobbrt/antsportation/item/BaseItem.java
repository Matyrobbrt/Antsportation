package com.matyrobbrt.antsportation.item;

import com.matyrobbrt.antsportation.compat.jei.JEIInfoProvider;
import com.matyrobbrt.antsportation.data.HasRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseItem extends Item implements HasRecipe, JEIInfoProvider {

    public BaseItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull List<Component> getInfo() {
        return List.of();
    }
}
