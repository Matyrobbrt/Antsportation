package com.matyrobbrt.antsportation.data.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.onetimejoin.OneTimeReward;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.BiConsumer;

public class OneTimeJoins implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;

    public OneTimeJoins(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
    }

    private void addEntries(BiConsumer<ResourceLocation, OneTimeReward> consumer) {
        consumer.accept(Antsportation.rl("guide_book"), new OneTimeReward("patchouli",
                PatchouliAPI.get().getBookStack(Antsportation.rl(Antsportation.MOD_ID))));
    }

    @Override
    public void run(HashCache pCache) {
        final var outputFolder = this.generator.getOutputFolder();
        final var loc = OneTimeReward.RESOURCE_KEY.location();
        final var directory = loc.getNamespace() + "/" + loc.getPath();

        final var resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", directory);
        addEntries(LamdbaExceptionUtils.rethrowBiConsumer((id, oneTimeJoinReward) -> {
            existingFileHelper.trackGenerated(id, resourceType);
            final var path = outputFolder.resolve(String.join("/", "data", id.getNamespace(), directory, id.getPath() + ".json"));
            final var encoded = OneTimeReward.CODEC.encodeStart(JsonOps.INSTANCE, oneTimeJoinReward)
                    .getOrThrow(false, msg -> LOGGER.error("Failed to encode {}: {}", path, msg));
            DataProvider.save(GSON, pCache, encoded, path);
        }));
    }

    @Override
    public String getName() {
        return "One Time Joins";
    }
}
