package com.matyrobbrt.antsportation.data.advancement

import groovy.transform.CompileStatic
import net.minecraft.advancements.Advancement
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.ExistingFileHelper

import java.util.function.Consumer

@CompileStatic
class AdvancementGenerator extends net.minecraft.data.advancements.AdvancementProvider {
    AdvancementGenerator(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn)
    }

    public final List<AdvancementProvider> providers = [
            new AntAdvancements() as AdvancementProvider
    ] as List<AdvancementProvider>

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        providers.each {it.register(consumer)}
    }
}
