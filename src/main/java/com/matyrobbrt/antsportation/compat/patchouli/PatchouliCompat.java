package com.matyrobbrt.antsportation.compat.patchouli;

import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        PatchouliAPI.get().registerFunction("antsportationconfig.server", (path, iStyleStack) -> {
            final var cfg = ServerConfig.BY_PATH.get(path);
            final var val = cfg.get();
            if (val instanceof StringRepresentable str) {
                return str.getSerializedName();
            }
            return val.toString();
        });
    }

}
