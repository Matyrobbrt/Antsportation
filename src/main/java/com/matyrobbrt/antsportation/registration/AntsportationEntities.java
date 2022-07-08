package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registry.ENTITY_TYPE_REGISTRY, Antsportation.MOD_ID);

    public static final RegistryObject<EntityType<AntQueenEntity>> ANT_QUEEN = ENTITIES.register("ant_queen",
            () -> EntityType.Builder.of(AntQueenEntity::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.1f)
                    .build(Antsportation.rlStr("ant_queen")));

    public static final RegistryObject<EntityType<AntSoldierEntity>> ANT_SOLDIER = ENTITIES.register("ant_soldier",
            () -> EntityType.Builder.of(AntSoldierEntity::new, MobCategory.CREATURE)
                    .sized(0.2f, 0.1f)
                    .build(Antsportation.rlStr("ant_soldier")));
}
