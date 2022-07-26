package com.matyrobbrt.antsportation.data.client

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.block.AntHillBlock
import com.matyrobbrt.antsportation.block.BoxerBlock
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationItems
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.*
import net.minecraftforge.common.data.ExistingFileHelper
import org.jetbrains.annotations.NotNull

import java.util.function.Supplier

import static com.matyrobbrt.antsportation.Antsportation.rl

class Models extends BlockStateProvider {
    Models(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Antsportation.MOD_ID, exFileHelper)
    }

    public static final Direction[] ROTATABLE_DIRECTIONS = new Direction[] {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
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
        simpleItem(AntsportationItems.MARKER)

        block(AntsportationBlocks.ANT_HILL) {
            final var base = modLoc('block/ant_hill_base')
            final var grassyModel = models()
                    .withExistingParent("${getLocation(AntsportationBlocks.ANT_HILL)}_grassy", base)
                    .texture('0', modLoc('block/ant_hill_grassy'))
                    .texture('particle', modLoc('block/ant_hill_grassy'))
            final var model = models()
                    .withExistingParent(getLocation(AntsportationBlocks.ANT_HILL), base)
                    .texture('0', modLoc('block/ant_hill'))
                    .texture('particle', modLoc('block/ant_hill'))
            itemModels().withExistingParent(getLocation(AntsportationBlocks.ANT_HILL), model.getLocation())
            forAllStates {
                if (it.getValue(AntHillBlock.IS_GRASSY) === true)
                    return new ConfiguredModel(grassyModel).selfArray()
                new ConfiguredModel(model).selfArray()
            }
        }

        block(AntsportationBlocks.ANT_JAR) {
            final var model = models().getExistingFile(modLoc('block/ant_jar'))
            final var confModels = new ConfiguredModel(model).selfArray()
            forAllStates(s -> confModels)
        }

        boxer(AntsportationBlocks.BOXER, modLoc('block/boxer_front'))
        boxer(AntsportationBlocks.UNBOXER, modLoc('block/unboxer_front'))
        blockItem(AntsportationBlocks.BOXER)
        blockItem(AntsportationBlocks.UNBOXER)
    }

    private void boxer(Supplier<? extends Block> block, ResourceLocation front) {
        final var side = modLoc('block/boxer_side')
        final var top = modLoc('block/boxer_top')
        final var bottom = mcLoc('block/stone')
        this.block(block) {
            partialState {
                for (final direction in ROTATABLE_DIRECTIONS) {
                    int rotationY = switch (direction) {
                        case Direction.EAST -> 90
                        case Direction.SOUTH -> 180
                        case Direction.WEST -> 270
                        default -> 0
                    }
                    with(BoxerBlock.FACING, direction).modelForState()
                    .modelFile(models().orientableWithBottom(
                            "block/${getLocation(block)}",
                            side, front, bottom, top
                    )).rotationY(rotationY).addModel()
                }
            }
        }
    }

    private void blockItem(Supplier<? extends Block> block) {
        final var loc = getLocation(block)
        itemModels().withExistingParent(loc, modLoc("block/$loc"))
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

    private VariantBlockStateBuilder block(Supplier<? extends Block> block, @DelegatesTo(value = VariantBlockStateBuilder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final var prov = getVariantBuilder(block.get())
        clos.setDelegate(prov)
        clos.call()
        return prov
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
    @SuppressWarnings('GrDeprecatedAPIUsage')
    private static String getLocation(ItemLike item) {
        Registry.ITEM.getKey(item.asItem()).getPath()
    }
}