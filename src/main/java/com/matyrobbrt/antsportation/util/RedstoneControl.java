package com.matyrobbrt.antsportation.util;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.BlockEntityProperty;
import net.minecraft.network.chat.Component;

public enum RedstoneControl {
    DISABLED(Translations.REDSTONE_DISABLED), HIGH(Translations.REDSTONE_HIGH),
    LOW(Translations.REDSTONE_LOW), PULSE(Translations.REDSTONE_PULSE);

    public static final BlockEntityProperty<RedstoneControl> PROPERTY = BlockEntityProperty.enumProperty(Antsportation.rl("redstone_control"), RedstoneControl.class);

    private final Translations translation;

    RedstoneControl(Translations translation) {
        this.translation = translation;
    }
    public Component getName() {
        return translation.translate();
    }
}
