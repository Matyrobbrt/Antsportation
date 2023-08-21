package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"unused", "SameParameterValue"})
public final class AntsportationSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Antsportation.MOD_ID);

    public static final RegistryObject<SoundEvent> PACKING = register("be.packing");

    public static final RegistryObject<SoundEvent> ANT_HURT = register("entity.ant_hurt");
    public static final RegistryObject<SoundEvent> ANT_DEATH = register("entity.ant_death");
    public static final RegistryObject<SoundEvent> ANT_WALK = register("entity.ant_walk");
    public static final RegistryObject<SoundEvent> ANT_ATTACK = register("entity.ant_attack");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Antsportation.rl(name)));
    }
}