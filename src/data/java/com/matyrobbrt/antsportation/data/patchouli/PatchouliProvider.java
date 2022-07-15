package com.matyrobbrt.antsportation.data.patchouli;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.lib.datagen.patchouli.page.CraftingRecipePage;
import com.matyrobbrt.lib.datagen.patchouli.page.SpotlightPage;
import com.matyrobbrt.lib.datagen.patchouli.page.TextPage;
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
    public final EnUdProvider enUd;

    public PatchouliProvider(DataGenerator generator) {
        super(generator, Antsportation.MOD_ID, "en_us", Antsportation.MOD_ID);
        this.enUd = new EnUdProvider(this, generator);
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
            .addMacro(new PatchouliMacro("<gold>", "$(#FFAA00)"))
            .addMacro(new PatchouliMacro("<aqua>", "$(#55FFFF)"))
            .setHeaderColor("FF0000");

    @PatchouliCategoryGen
    public static final PatchouliCategory MACHINES_CATEGORY = new PatchouliCategory("Machines", "machines", "Information about Antsportation machines", AntsportationBlocks.BOXER.get());

    @Override
    public void addEntries() {
        entries.add(entry(MACHINES_CATEGORY, "Speed Upgrade", AntsportationItems.SPEED_UPGRADE)
                .addPage(new SpotlightPage(AntsportationItems.SPEED_UPGRADE.get(), multiline(
                        "<item>Speed Upgrades</> can be put in boxing machines (<item>Boxers</> or <item>Unboxers</>) in <br>order to make them faster at the cost of energy usage.",
                        "<br>Stats:",
                        "\u2022 <gold>Energy usage per upgrade</>: $(scfg/boxing.upgradeEnergyUsage) FE",
                        "\u2022 <gold>Process duration decrease per upgrade</>: $(scfg/boxing.upgradeReduction) ticks"
                )))
                .addPage(getCraftingRecipe(AntsportationItems.SPEED_UPGRADE)));
        entries.add(entry(MACHINES_CATEGORY, "Boxer", AntsportationBlocks.BOXER)
                .addPage(new SpotlightPage(AntsportationBlocks.BOXER.get(), multiline(
                        "<item>Boxers</> are special machines which pack item into boxes.",
                        "Items are inserted into <item>Boxes</> from the top, and boxes can be extracted from the bottom. By default, the box will be locked into its slot, preventing extraction. That behaviour can be customized in the Configuration menu, accessible via the C button in the top right corner of the Boxer."
                )))
                .addPage(new TextPage("Stats", multiline(
                        statsEntry("Uses energy", "boxing.useEnergy"),
                        statsEntry("Energy Capacity", "boxing.energyCapacity"),
                        "\u2022 <gold>Energy I/O rate</>: $(antsportation_boxing_io)",
                        statsEntry("Ticks per operation", "boxing.baseNeededTicks"),
                        "\u2022 <gold>Base energy usage</>: $(scfg/boxing.baseUsedEnergy) FE"
                )))
                .addPage(new TextPage("Configuration", multiline(
                        "\u2022 <aqua>Content-based ejection</>:",
                        "The percentage a box needs to be filled in order to allow extraction. <gold>0</> disables this functionality.<br>",
                        "\u2022 <aqua>Redstone-based ejection</>:",
                        "Decides when box extraction should be allowed, based on redstone signal."
                )))
                .addPage(getCraftingRecipe(AntsportationBlocks.BOXER)));
    }

    private static String statsEntry(String name, String config) {
        return "\u2022 <gold>%s</>: $(scfg/%s)".formatted(name, config);
    }

    private static PatchouliEntry entry(PatchouliCategory category, String displayName, Supplier<? extends ItemLike> item) {
        return new PatchouliEntry(Antsportation.rlStr(category.fileName), displayName, item.get());
    }

    private static CraftingRecipePage getCraftingRecipe(Supplier<? extends ItemLike> item) {
        return new CraftingRecipePage(Registry.ITEM.getKey(item.get().asItem()).toString());
    }

    private static String multiline(String... strings) {
        final var builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i].replace("\n", "<br>"));
            if (i < (strings.length - 1))
                builder.append("<br>");
        }
        return builder.toString();
    }
}
