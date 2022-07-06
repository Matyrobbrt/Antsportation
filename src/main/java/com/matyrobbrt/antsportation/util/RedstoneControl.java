package com.matyrobbrt.antsportation.util;

import net.minecraft.network.chat.Component;

public enum RedstoneControl {
    DISABLED(Translations.REDSTONE_DISABLED), HIGH(Translations.REDSTONE_HIGH),
    LOW(Translations.REDSTONE_LOW), PULSE(Translations.REDSTONE_PULSE);

    private final Translations translation;

    RedstoneControl(Translations translation) {
        this.translation = translation;
    }
    public Component getName() {
        return translation.translate();
    }
}
