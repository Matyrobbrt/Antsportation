package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class AntsportationSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registry.SOUND_EVENT_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<SoundEvent> PACKING = register("be.packing");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(Antsportation.rl(name)));
    }
}