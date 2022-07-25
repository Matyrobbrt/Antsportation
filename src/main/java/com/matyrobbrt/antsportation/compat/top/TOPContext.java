package com.matyrobbrt.antsportation.compat.top;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public interface TOPContext {
    Player getPlayer();
    Mode getMode();

    void addStackWithProgressElement(ItemStack stack, int progress, StackWithProgressElement.Direction direction);
    void text(Component text);

    enum Mode {
        NORMAL,
        EXTENDED,
        DEBUG
    }
}
