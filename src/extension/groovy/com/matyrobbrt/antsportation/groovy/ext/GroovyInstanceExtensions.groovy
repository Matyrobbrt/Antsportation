package com.matyrobbrt.antsportation.groovy.ext

import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.generators.*

import java.nio.file.Files
import java.nio.file.Path

@CompileStatic
@SuppressWarnings('unused')
class GroovyInstanceExtensions {
    static boolean asBoolean(Path path) {
        Files.exists(path)
    }
    static <T extends ModelBuilder<T>> ModelBuilder<T> layer0(ModelBuilder<T> self, ResourceLocation texture) {
        self.texture('layer0', texture)
    }
    static ItemModelBuilder override(ItemModelBuilder self, @DelegatesTo(value = ItemModelBuilder.OverrideBuilder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final var ov = self.override()
        clos.resolveStrategy = Closure.DELEGATE_FIRST
        clos.delegate = ov
        clos.call ov
        self
    }
    static ConfiguredModel asType(BlockModelBuilder builder, Class clazz) {
        if (clazz == ConfiguredModel)
            new ConfiguredModel(builder)
        null
    }

    static VariantBlockStateBuilder.PartialBlockstate addModels(VariantBlockStateBuilder.PartialBlockstate self, BlockModelBuilder... models) {
        self.addModels(models.collect(ConfiguredModel.&new).toArray(ConfiguredModel[]::new))
    }
    static void partialState(VariantBlockStateBuilder self, @DelegatesTo(value = VariantBlockStateBuilder.PartialBlockstate, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final var partial = self.partialState()
        clos.resolveStrategy = Closure.DELEGATE_FIRST
        clos.delegate = partial
        clos.call()
    }

    static <T> T[] array(List<T> self) {
        self as T[]
    }
    static <T> T[] selfArray(T self) {
        [self] as T[]
    }
}
