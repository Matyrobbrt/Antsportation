package com.matyrobbrt.antsportation.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.UnaryOperator;

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
        if (count >= 1_000_000) {
            final var n = ((double) count) / 1_000_000;
            return (Math.round(n * 10.0) / 10.0) + "M";
        } else if (count > 1000) {
            final var n = ((double) count) / 1000;
            return (Math.round(n * 10.0) / 10.0) + "k";
        }
        return String.valueOf(count);
    }
    public static MutableComponent getComponent(FormattedText formattedText, Style style) {
        MutableComponent comp = textComponent("");
        final var res = formattedText.visit((final Style pStyle, final String pContent) -> Optional.of(Component.literal(pContent).withStyle(pStyle)), style);
        if (res.isPresent())
            comp = comp.append(res.get());
        return comp;
    }
    public static MutableComponent textComponent(Object text) {
        return Component.literal(text.toString());
    }
    public static MutableComponent textComponent(Object text, @Nonnull UnaryOperator<Style> style) {
        return Component.literal(text.toString()).withStyle(style);
    }

    public static boolean checkTileStillValid(Player pPlayer, @Nullable BlockEntity tile) {
        if (tile == null)
            return false;
        if (pPlayer.level.getBlockEntity(tile.getBlockPos()) != tile) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double) tile.getBlockPos().getX() + 0.5D, (double) tile.getBlockPos().getY() + 0.5D, (double) tile.getBlockPos().getZ() + 0.5D) > 64.0D);
        }
    }

    public static String calculateNumeralType(int x) {
        final var last = x % 10;
        return switch (last) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public static CompoundTag writeVec3(Vec3 vec3) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putDouble("X", vec3.x);
        compoundtag.putDouble("Y", vec3.y);
        compoundtag.putDouble("Z", vec3.z);
        return compoundtag;
    }

    public static Vec3 readVec3(CompoundTag tag) {
        return new Vec3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
    }

    // Automatic en_ud generation
    // Inspired from https://github.com/Tropicraft/Tropicraft/blob/a600e6bce7f1d131eb2a7d1e6fa36e1718d2014f/src/main/java/net/tropicraft/core/client/data/TropicraftLangProvider.java#L526-L567

    private static final String NORMAL_CHARS =
            /* lowercase */ "abcdefghijklmn\u00F1opqrstuvwxyz" +
            /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            /*  numbers  */ "0123456789" +
            /*  special  */ "_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS =
            /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
            /* uppercase */ "\u2C6F\u15FA\u0186\u15E1\u018E\u2132\u2141HI\u017F\u029E\uA780WNO\u0500\u1F49\u1D1AS\u27D8\u2229\u039BMX\u028EZ" +
            /*  numbers  */ "0\u0196\u1105\u0190\u3123\u03DB9\u312586" +
            /*  special  */ "\u203E'\u061B\u02D9\u00BF\u00A1/\\,";

    public static char toUpsideDown(char c) {
        int lookup = NORMAL_CHARS.indexOf(c);
        if (lookup >= 0) {
            return UPSIDE_DOWN_CHARS.charAt(lookup);
        }
        return c;
    }

    public static String toUpsideDown(@Nullable String normal) {
        if (normal == null)
            return null;
        char[] ud = new char[normal.length()];
        for (int i = 0; i < normal.length(); i++) {
            char c = normal.charAt(i);
            if (c == '%') {
                StringBuilder fmtArg = new StringBuilder();
                while (Character.isDigit(c) || c == '%' || c == '$' || c == 's' || c == 'd') { // TODO this is a bit lazy
                    fmtArg.append(c);
                    i++;
                    c = i == normal.length() ? 0 : normal.charAt(i);
                }
                i--;
                for (int j = 0; j < fmtArg.length(); j++) {
                    ud[normal.length() - 1 - i + j] = fmtArg.charAt(j);
                }
                continue;
            }
            int lookup = NORMAL_CHARS.indexOf(c);
            if (lookup >= 0) {
                c = UPSIDE_DOWN_CHARS.charAt(lookup);
            }
            ud[normal.length() - 1 - i] = c;
        }
        return new String(ud);
    }
}
