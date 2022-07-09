package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Models extends BlockStateProvider {
    public Models(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Antsportation.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (final var box : BoxItem.BoxTier.values()) {
            itemModels().withExistingParent(Registry.ITEM.getKey(box.asItem()).getPath(), mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/box"))
                    .texture("layer1", modLoc("item/box_overlay"));
        }

        ItemModelBuilder jarWithAnt = itemModels().withExistingParent(Registry.ITEM.getKey(AntsportationItems.ANT_JAR.get()).getPath() + "filled", mcLoc("item/generated"))
                        .texture("layer0", modLoc("item/queen_ant_jar"));

        itemModels().withExistingParent(Registry.ITEM.getKey(AntsportationItems.ANT_JAR.get()).getPath(), mcLoc("item/generated"))
                .override().predicate(Antsportation.rl("filled"), 1).model(jarWithAnt).end().texture("layer0", modLoc("item/glass_jar"));


    }
}
