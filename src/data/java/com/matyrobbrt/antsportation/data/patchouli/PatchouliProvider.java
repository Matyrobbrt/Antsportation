package com.matyrobbrt.antsportation.data.patchouli;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.lib.datagen.patchouli.page.CraftingRecipePage;
import com.matyrobbrt.lib.datagen.patchouli.page.SpotlightPage;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliMacro;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@SuppressWarnings("SameParameterValue")
public class PatchouliProvider extends com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider {
    public PatchouliProvider(DataGenerator generator) {
        super(generator, Antsportation.MOD_ID, "en_us", Antsportation.MOD_ID);
    }

    @PatchouliBookGen
    public static final PatchouliBook BOOK = new PatchouliBook(Translations.GUIDE_BOOK.key, Translations.GUIDE_BOOK_LANDING_TEXT.key)
            .setTab(Antsportation.TAB.getRecipeFolderName())
            .setBookTexture(new ResourceLocation("patchouli", "textures/gui/book_red.png"))
            .setModel(new ResourceLocation("patchouli", "book_red"))
            .setIndexIcon(Items.WRITABLE_BOOK).showProgress(false)
            .setVersion(1)
            .addDefaultMacros()
            .addMacro(new PatchouliMacro("$(scfg/", "$(antsportationconfig.server:"))
            .setHeaderColor("FF0000");

    @PatchouliCategoryGen
    public static final PatchouliCategory MACHINES_CATEGORY = new PatchouliCategory("Machines", "machines", "Information about Antsportation machines", AntsportationBlocks.BOXER.get());

    @Override
    public void addEntries() {
        entries.add(entry(MACHINES_CATEGORY, "Speed Upgrade", AntsportationItems.SPEED_UPGRADE)
                .addPage(new SpotlightPage(AntsportationItems.SPEED_UPGRADE.get(), "<item>Speed Upgrades</> can be put in boxing machines (<item>Boxers</> or <item>Unboxers</>) in order to make them faster at the cost of energy usage."))
                .addPage(getCraftingRecipe(AntsportationItems.SPEED_UPGRADE)));
    }

    private PatchouliEntry entry(PatchouliCategory category, String displayName, Supplier<? extends ItemLike> item) {
        return new PatchouliEntry(Antsportation.rlStr(category.fileName), displayName, item.get());
    }

    private CraftingRecipePage getCraftingRecipe(Supplier<? extends ItemLike> item) {
        return new CraftingRecipePage(Registry.ITEM.getKey(item.get().asItem()).toString());
    }

}
