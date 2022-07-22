package com.matyrobbrt.antsportation.world;

import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class AntEntityGen {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToForrestBiomes(event, AntsportationEntities.ANT_SOLDIER.get(),
                1400, 1, 4);
        addEntityToForrestBiomes(event, AntsportationEntities.ANT_QUEEN.get(),
                800, 1, 1);
    }


    private static void addEntityToForrestBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = event.getCategory().compareTo(Biome.BiomeCategory.FOREST) == 0;

        if(isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
        base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
    }

}
