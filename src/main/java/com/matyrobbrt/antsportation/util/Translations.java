package com.matyrobbrt.antsportation.util;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public enum Translations {
    EMPTY("tooltip", "empty", "Empty"),
    CONTENTS("tooltip", "contents", "Contents:"),
    AND_MORE("tooltip", "and_more", "... and %s more"),
    MORE("tooltip", "more", "more");
    public final String key;
    public final String englishTranslation;

    Translations(String key, String path, String englishTranslation) {
        this.key = key + "." + Antsportation.MOD_ID + "." + path;
        this.englishTranslation = englishTranslation;
    }

    public MutableComponent translate(Object... args) {
        return new TranslatableComponent(key, args);
    }
}
