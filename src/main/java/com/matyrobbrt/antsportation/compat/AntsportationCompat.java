package com.matyrobbrt.antsportation.compat;

import com.matyrobbrt.antsportation.compat.patchouli.PatchouliCompat;
import com.matyrobbrt.antsportation.compat.top.AntsportationTOPProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntsportationCompat {
    public static final Logger LOGGER = LoggerFactory.getLogger("AntsportationCompat");

    public static void init(final IEventBus modBus) {
        ifLoaded("patchouli", () -> {
            if (FMLEnvironment.dist == Dist.CLIENT)
                modBus.register(PatchouliCompat.class);
        });
        ifLoaded("theoneprobe", () -> modBus.addListener((final InterModEnqueueEvent event) -> {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", AntsportationTOPProvider::new);
        }));
    }

    private static void ifLoaded(String modId, Runnable toRun) {
        if (ModList.get().isLoaded(modId)) {
            toRun.run();
            LOGGER.debug("Initialised {} compat!", modId);
        }
    }
}
