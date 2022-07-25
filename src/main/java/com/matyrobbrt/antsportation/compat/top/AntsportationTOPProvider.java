package com.matyrobbrt.antsportation.compat.top;

import com.matyrobbrt.antsportation.Antsportation;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class AntsportationTOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    public static final ResourceLocation ID = Antsportation.rl("top");

    static IItemStyle defaultItemStyle;

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(this);

        probe.registerElementFactory(new StackWithProgressElement.Factory());

        defaultItemStyle = probe.getStyleManager().itemStyleDefault();
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
        final var pos = iProbeHitData.getPos();
        if (level.getBlockEntity(pos) instanceof TOPInfoDriver driver) {
            driver.addInfo(new TOPContext() {
                @Override
                public Player getPlayer() {
                    return player;
                }

                @Override
                public Mode getMode() {
                    return switch (probeMode) {
                        case NORMAL -> Mode.NORMAL;
                        case DEBUG -> Mode.DEBUG;
                        case EXTENDED -> Mode.EXTENDED;
                    };
                }

                @Override
                public void text(Component text) {
                    iProbeInfo.text(text);
                }

                @Override
                public void addStackWithProgressElement(ItemStack stack, int progress, StackWithProgressElement.Direction direction) {
                    iProbeInfo.element(new StackWithProgressElement(stack, progress, direction));
                }
            });
        }
    }
}
