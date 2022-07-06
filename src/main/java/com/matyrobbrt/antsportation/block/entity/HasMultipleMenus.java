package com.matyrobbrt.antsportation.block.entity;

import net.minecraft.world.MenuProvider;

import javax.annotation.Nullable;

public interface HasMultipleMenus {
    @Nullable
    MenuProvider getMenu(byte index);
}
