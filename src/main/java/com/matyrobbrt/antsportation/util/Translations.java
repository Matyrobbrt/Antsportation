package com.matyrobbrt.antsportation.util;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public enum Translations {
    EMPTY("tooltip", "empty", "Empty"),
    CONTENTS("tooltip", "contents", "Contents:"),
    AND_MORE("tooltip", "and_more", "... and %s more"),
    MORE("tooltip", "more", "more"),
    STORED_ENERGY("tooltip", "energy.stored", "Stored energy: %s / %s"),
    CONFIGURATION("tooltip", "configuration", "Open configuration"),
    TOOLTIP_TYPES("tooltip", "types", "%s / %s types"),
    TOOLTIP_ITEMS("tooltip", "items", "%s / %s items"),

    SPEED_UPGRADE_TOOLTIP("tooltip.item", "speed_upgrade", "Insert in a Boxer in order to make it faster."),
    SPEED_UPGRADE_TOOLTIP2("tooltip.item", "speed_upgrade_2", "Decreases process duration by %s ticks."),
    SPEED_UPGRADE_TOOLTIP3("tooltip.item", "speed_upgrade_3", "Increases energy usage: %s more FE / tick"),

    EJECT_WHEN("button", "eject_when", "Eject when:"),
    REDSTONE_CONTROL("button", "redstone_control", "Redstone control:"),

    BOXER_EJECT_WHEN_TOOLTIP("boxer_tooltip", "eject_when", "The percentage a box needs to be filled in order to allow extraction. 0 disables this functionality"),
    BOXER_REDSTONE_CONTROL_TOOLTIP("boxer_tooltip", "redstone_control", "Decides when box extraction should be allowed, based on redstone signal. Use 'DISABLED' in order to keep the box locked until the ejection percent is hit."),

    REDSTONE_DISABLED("redstone", "disabled", "Disabled"),
    REDSTONE_PULSE("redstone", "pulse", "Pulse"),
    REDSTONE_HIGH("redstone", "high", "High"),
    REDSTONE_LOW("redstone", "low", "Low"),

    BOXER("gui", "boxer", "Boxer"),
    CONFIGURATION_GUI("gui", "configuration", "Configuration: %s"),

    SUBTITLE_PACKING("subtitle", "packing", "Packing")
    ;

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
