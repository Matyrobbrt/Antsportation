package com.matyrobbrt.antsportation.groovy.ext

import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.client.model.generators.ModelBuilder

import java.nio.file.Files
import java.nio.file.Path

@CompileStatic
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
}
