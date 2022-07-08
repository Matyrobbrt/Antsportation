package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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
    }
}
