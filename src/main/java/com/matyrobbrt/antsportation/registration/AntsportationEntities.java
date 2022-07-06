package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registry.ENTITY_TYPE_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<EntityType<AntQueenEntity>> ANT_QUEEN = ENTITIES.register("ant_queen",
            () -> EntityType.Builder.of(AntQueenEntity::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.1f)
                    .build(new ResourceLocation(Antsportation.MOD_ID, "ant_queen").toString()));
}
