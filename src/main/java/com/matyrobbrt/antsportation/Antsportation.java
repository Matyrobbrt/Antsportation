package com.matyrobbrt.antsportation;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Antsportation.MOD_ID)
public class Antsportation {
    public static final String MOD_ID = "antsportation";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Antsportation() {
        LOGGER.debug("Initialising Antsportation");
        final var bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.BLOCKS.register(bus);
        Registration.ITEMS.register(bus);
        Registration.ENTITIES.register(bus);
        LOGGER.debug("Antsportation initialized");
    }
}
