package com.matyrobbrt.antsportation.groovy.ext

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.DisplayInfo
import net.minecraft.advancements.FrameType
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraftforge.client.model.generators.*

import javax.annotation.Nullable
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

    static Advancement.Builder display(Advancement.Builder self, ItemStack pStack, Object pTitle, Object pDescription, @Nullable ResourceLocation pBackground, FrameType pFrame, boolean pShowToast, boolean pAnnounceToChat, boolean pHidden) {
        return self.display(pStack, new TranslatableComponent(pTitle.toString()), new TranslatableComponent(pDescription.toString()), pBackground, pFrame, pShowToast, pAnnounceToChat, pHidden);
    }
    static Advancement.Builder display(Advancement.Builder self, ItemLike pIcon, Object pTitle, Object pDescription, @Nullable ResourceLocation pBackground, FrameType pFrame, boolean pShowToast, boolean pAnnounceToChat, boolean pHidden) {
        return self.display(pIcon, new TranslatableComponent(pTitle.toString()), new TranslatableComponent(pDescription.toString()), pBackground, pFrame, pShowToast, pAnnounceToChat, pHidden);
    }

    static LootTable.Builder pool(LootTable.Builder self, @DelegatesTo(value = LootPool.Builder, strategy = Closure.DELEGATE_FIRST) Closure closure) {
        final pool = LootPool.lootPool()
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = pool
        closure.call(pool)
        return self.withPool(pool)
    }

    static LootPool.Builder item(LootPool.Builder self, String name, ItemLike item, int count) {
        return self
            .name(name)
            .setRolls(ConstantValue.exactly(count))
            .add(LootItem.lootTableItem(item))
    }
    static LootPool.Builder items(LootPool.Builder self, String name, ItemLike... items) {
        self
            .name(name)
            .setRolls(ConstantValue.exactly(1))
        items.each {self.add(LootItem.lootTableItem(it))}
        return self
    }
}
