package com.matyrobbrt.antsportation.data.client

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationItems
import net.minecraft.core.Registry
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.ItemLike
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import org.jetbrains.annotations.NotNull

import java.util.function.Supplier

import static com.matyrobbrt.antsportation.Antsportation.rl

class Models extends BlockStateProvider {
    Models(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Antsportation.MOD_ID, exFileHelper)
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(AntsportationBlocks.ANT_NEST.get())
        simpleBlockItem(AntsportationBlocks.ANT_NEST.get(), new ModelFile.ExistingModelFile(modLoc('block/ant_nest'), models().existingFileHelper))

        for (final box in BoxItem.BoxTier.values()) {
            item(() -> box) {
                texture('layer0', modLoc('item/box'))
                texture('layer1', modLoc('item/box_overlay'))
            }
        }

        item(AntsportationItems.ANT_JAR) {
            final var withAnt = item("${getLocation(AntsportationItems.ANT_JAR)}_filled") {
                layer0(modLoc('item/queen_ant_jar'))
            }

            override {
                predicate(rl('filled'), 1)
                    .model(withAnt)
            }

            layer0(modLoc('item/glass_jar'))
        }

        spawnEgg(AntsportationItems.ANT_QUEEN_SPAWN_EGG)
        spawnEgg(AntsportationItems.ANT_SOLDIER_SPAWN_EGG)
        spawnEgg(AntsportationItems.ANT_WORKER_SPAWN_EGG)

        simpleItem(AntsportationItems.SPEED_UPGRADE)
    }
    
    private ItemModelBuilder item(String location, @DelegatesTo(value = ItemModelBuilder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final var prov = itemModels().withExistingParent(location, mcLoc('item/generated'))
        clos.resolveStrategy = Closure.DELEGATE_FIRST
        clos.setDelegate(prov)
        clos.call(prov)
        return prov
    }
    private ItemModelBuilder item(Supplier<? extends ItemLike> item, @DelegatesTo(value = ItemModelBuilder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        this.item(getLocation(item), clos)
    }

    @SuppressWarnings('SameParameterValue')
    private void simpleItem(Supplier<? extends ItemLike> item) {
        final var loc = getLocation(item)
        this.item(item) {
            layer0(modLoc('item/' + loc))
        }
    }

    private void spawnEgg(Supplier<? extends ItemLike> item) {
        itemModels().withExistingParent(getLocation(item), mcLoc('item/template_spawn_egg'))
    }

    @NotNull
    private static String getLocation(Supplier<? extends ItemLike> item) {
        getLocation(item.get())
    }

    @NotNull
    private static String getLocation(ItemLike item) {
        Registry.ITEM.getKey(item.asItem()).getPath()
    }
}