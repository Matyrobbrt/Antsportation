package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;

public class AntsportationEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registry.ENTITY_TYPE_REGISTRY, Antsportation.MOD_ID);
}
