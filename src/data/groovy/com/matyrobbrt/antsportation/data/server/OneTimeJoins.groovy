package com.matyrobbrt.antsportation.data.server


import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.compat.patchouli.PatchouliCompat
import com.matyrobbrt.antsportation.onetimejoin.OneTimeReward
import com.mojang.logging.LogUtils
import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraftforge.common.data.ExistingFileHelper
import org.slf4j.Logger

import java.util.function.BiConsumer

class OneTimeJoins implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger()

    private final DataGenerator generator
    private final ExistingFileHelper existingFileHelper

    OneTimeJoins(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        this.generator = generator
        this.existingFileHelper = existingFileHelper
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    private void addEntries(BiConsumer<ResourceLocation, OneTimeReward> consumer) {
        consumer.accept(Antsportation.rl("guide_book"), new OneTimeReward("patchouli",
                PatchouliCompat.getBook()))
    }

    @Override
    void run(CachedOutput pCache) {
        final var outputFolder = this.generator.getOutputFolder()
        final var loc = OneTimeReward.RESOURCE_KEY.location()
        final var directory = loc.getNamespace() + "/" + loc.getPath()

        final var resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", directory)
        addEntries((id, oneTimeJoinReward) -> {
            existingFileHelper.trackGenerated(id, resourceType)
            final var path = outputFolder.resolve(String.join("/", "data", id.getNamespace(), directory, id.getPath() + ".json"))
            final var encoded = OneTimeReward.CODEC.encodeStart(JsonOps.INSTANCE, oneTimeJoinReward)
                    .getOrThrow(false, msg -> LOGGER.error("Failed to encode {}: {}", path, msg))
            //noinspection UnnecessaryQualifiedReference
            DataProvider.saveStable(pCache, encoded, path)
        })
    }

    @Override
    String getName() {
        return "One Time Joins"
    }
}
