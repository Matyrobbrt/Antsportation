package com.matyrobbrt.antsportation.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Optional;

public class Utils {
    public static ItemStack withCount(ItemStack stack, int count) {
        final var copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
    public static void clearTag(CompoundTag compoundTag) {
        final var keys = new HashSet<>(compoundTag.getAllKeys());
        keys.forEach(compoundTag::remove);
    }
    public static String getCompressedCount(int count) {
        if (count > 1000) {
            final var n = ((double) count) / 1000;
            return String.valueOf(Math.round(n * 10.0) / 10.0) + "k";
        }
        return String.valueOf(count);
    }
    public static MutableComponent getComponent(FormattedText formattedText, Style style) {
        MutableComponent comp = textComponent("");
        final var res = formattedText.visit((final Style pStyle, final String pContent) -> Optional.of(new TextComponent(pContent).withStyle(pStyle)), style);
        if (res.isPresent())
            comp = comp.append(res.get());
        return comp;
    }
    public static MutableComponent textComponent(String text) {
        return new TextComponent(text);
    }
}
