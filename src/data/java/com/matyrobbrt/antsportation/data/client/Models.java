package com.matyrobbrt.antsportation.data.client;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Models extends BlockStateProvider {
    public Models(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Antsportation.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(AntsportationBlocks.ANT_NEST.get());
        simpleBlockItem(AntsportationBlocks.ANT_NEST.get(), new ModelFile.ExistingModelFile(modLoc("block/ant_nest"), models().existingFileHelper));

        for (final var box : BoxItem.BoxTier.values()) {
            itemModels().withExistingParent(getLocation(box), mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/box"))
                    .texture("layer1", modLoc("item/box_overlay"));
        }

        ItemModelBuilder jarWithAnt = itemModels().withExistingParent(getLocation(AntsportationItems.ANT_JAR) + "filled", mcLoc("item/generated"))
                        .texture("layer0", modLoc("item/queen_ant_jar"));

        itemModels().withExistingParent(getLocation(AntsportationItems.ANT_JAR), mcLoc("item/generated"))
                .override().predicate(Antsportation.rl("filled"), 1).model(jarWithAnt).end().texture("layer0", modLoc("item/glass_jar"));

        spawnEgg(AntsportationItems.ANT_QUEEN_SPAWN_EGG);
        spawnEgg(AntsportationItems.ANT_SOLDIER_SPAWN_EGG);
        spawnEgg(AntsportationItems.ANT_WORKER_SPAWN_EGG);
    }

    private void spawnEgg(Supplier<? extends Item> item) {
        itemModels().withExistingParent(getLocation(item), mcLoc("item/template_spawn_egg"));
    }

    @NotNull
    private String getLocation(Supplier<? extends Item> item) {
        return getLocation(item.get());
    }

    @NotNull
    private String getLocation(ItemLike item) {
        return Registry.ITEM.getKey(item.asItem()).getPath();
    }
}
