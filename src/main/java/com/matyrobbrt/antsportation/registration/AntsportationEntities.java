package com.matyrobbrt.antsportation.registration;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.entity.HillAntSoldierEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AntsportationEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Antsportation.MOD_ID);

    public static final RegistryObject<EntityType<AntQueenEntity>> ANT_QUEEN = ENTITIES.register("ant_queen",
            () -> EntityType.Builder.of(AntQueenEntity::new, MobCategory.CREATURE)
                    .fireImmune()
                    .sized(0.7f, 0.4f)
                    .build(Antsportation.rlStr("ant_queen")));

    public static final RegistryObject<EntityType<AntSoldierEntity>> ANT_SOLDIER = ENTITIES.register("ant_soldier",
            () -> EntityType.Builder.of(AntSoldierEntity::new, MobCategory.CREATURE)
                    .fireImmune()
                    .sized(1f, 0.4f)
                    .build(Antsportation.rlStr("ant_soldier")));
    public static final RegistryObject<EntityType<HillAntSoldierEntity>> HILL_ANT_SOLDIER = ENTITIES.register("hill_ant_soldier",
            () -> EntityType.Builder.of(HillAntSoldierEntity::new, MobCategory.CREATURE)
                    .fireImmune()
                    .sized(1f, 0.4f)
                    .build(Antsportation.rlStr("hill_ant_soldier")));

    public static final RegistryObject<EntityType<AntWorkerEntity>> ANT_WORKER = ENTITIES.register("ant_worker",
            () -> EntityType.Builder.of(AntWorkerEntity::new, MobCategory.CREATURE)
                    .fireImmune()
                    .sized(0.5f, 0.4f)
                    .build(Antsportation.rlStr("ant_worker")));
}
