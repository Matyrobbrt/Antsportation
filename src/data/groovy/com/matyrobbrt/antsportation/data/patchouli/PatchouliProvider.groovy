//file:noinspection GrDeprecatedAPIUsage
package com.matyrobbrt.antsportation.data.patchouli

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationItems
import com.matyrobbrt.antsportation.util.Translations
import com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider.PatchouliBookGen
import com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider.PatchouliCategoryGen
import com.matyrobbrt.lib.datagen.patchouli.page.CraftingRecipePage
import com.matyrobbrt.lib.datagen.patchouli.page.SpotlightPage
import com.matyrobbrt.lib.datagen.patchouli.page.TextPage
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliMacro
import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraftforge.registries.ForgeRegistries
import org.jetbrains.annotations.Nullable

import javax.annotation.ParametersAreNonnullByDefault
import java.util.function.Supplier

@CompileStatic
@ParametersAreNonnullByDefault
@SuppressWarnings([ 'SameParameterValue', 'unused' ])
class PatchouliProvider extends com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider {
    public final EnUdProvider enUd

    PatchouliProvider(DataGenerator generator) {
        super(generator, Antsportation.MOD_ID, 'en_us', Antsportation.MOD_ID)
        this.enUd = new EnUdProvider(this, generator)
    }

    @PatchouliBookGen
    public static final PatchouliBook BOOK = new PatchouliBook(Translations.GUIDE_BOOK.key, Translations.GUIDE_BOOK_LANDING_TEXT.key)
            .setTab(Antsportation.TAB.getRecipeFolderName())
            .setBookTexture(new ResourceLocation('patchouli', 'textures/gui/book_red.png'))
            .setModel(new ResourceLocation('patchouli', 'book_red'))
            .setIndexIcon(Items.WRITABLE_BOOK).showProgress(false)
            .setVersion(1)
            .addDefaultMacros()
            .addMacro(new PatchouliMacro('$(scfg/', '$(antsportationconfig.server:'))
            .addMacro(new PatchouliMacro('<gold>', '$(#FFAA00)'))
            .addMacro(new PatchouliMacro('<aqua>', '$(#55FFFF)'))
            .addMacro(new PatchouliMacro('<blue>', '$(#5555FF)'))
            .setHeaderColor('FF0000')

    @PatchouliCategoryGen
    public static final PatchouliCategory MACHINES_CATEGORY = new PatchouliCategory('Machines', 'machines', 'Information about Antsportation machines', AntsportationBlocks.BOXER.get())
    @PatchouliCategoryGen
    public static final PatchouliCategory ITEMS_CATEGORY = new PatchouliCategory('Items', 'items', 'Information about Antsportation items', AntsportationBlocks.ANT_JAR.get())
    @PatchouliCategoryGen
    public static final PatchouliCategory BLOCKS_CATEGORY = new PatchouliCategory('Blocks', 'blocks', 'Information about Antsportation blocks', AntsportationBlocks.ANT_NEST.get())
    @PatchouliCategoryGen
    public static final PatchouliCategory WORLD_CATEGORY = new PatchouliCategory('World', 'world','Information about Antsportation world generation', AntsportationBlocks.ANT_HILL.get())
    @PatchouliCategoryGen
    public static final PatchouliCategory OTHER_CATEGORY = new PatchouliCategory('Other', 'other', 'Information about things that dont fit in the other categories', AntsportationItems.MARKER.get())
    @PatchouliCategoryGen
    public static final PatchouliCategory ENTITY_CATEGORY = new PatchouliCategory('Entity', 'entities', 'Information about the entities in Antsportation', AntsportationItems.ANT_QUEEN_SPAWN_EGG.get())

    @Override
    void addEntries() {
        entry(MACHINES_CATEGORY) {
            displayName 'Speed Upgrade'
            icon AntsportationItems.SPEED_UPGRADE
            addPage(new SpotlightPage(AntsportationItems.SPEED_UPGRADE.get(), multiline(
                    '<item>Speed Upgrades</> can be put in boxing machines (<item>Boxers</> or <item>Unboxers</>) in <br>order to make them faster at the cost of energy usage.',
                    '<br>Stats:',
                    '\u2022 <gold>Energy usage per upgrade</>: $(scfg/boxing.upgradeEnergyUsage) FE',
                    '\u2022 <gold>Process duration decrease per upgrade</>: $(scfg/boxing.upgradeReduction) ticks'
            )))
            addPage(getCraftingRecipe(AntsportationItems.SPEED_UPGRADE))
        }

        final def boxingStatsPage = new TextPage('Stats', multiline(
                statsEntry('Uses energy', 'boxing.useEnergy'),
                statsEntry('Energy Capacity', 'boxing.energyCapacity'),
                '\u2022 <gold>Energy I/O rate</>: $(antsportation_boxing_io) FE / t',
                statsEntry('Ticks per operation', 'boxing.baseNeededTicks'),
                statsEntry('Base energy usage', 'boxing.baseUsedEnergy', 'FE / t')
        ))
        entry(MACHINES_CATEGORY) {
            displayName 'Boxer'
            icon AntsportationBlocks.BOXER
            addPage(new SpotlightPage(AntsportationBlocks.BOXER.get(), multiline(
                    '<item>Boxers</> are special machines which pack item into boxes.',
                    'Items are inserted into <item>Boxes</> from the top, and boxes can be extracted from the bottom. By default, the box will be locked into its slot, preventing extraction. That behaviour can be customized in the Configuration menu, accessible via the C button in the top right corner of the Boxer.'
            )))
            addPage(boxingStatsPage)
            addPage(new TextPage('Configuration', multiline(
                    '\u2022 <blue>Content-based ejection</>:',
                    'The percentage a box needs to be filled in order to allow extraction. <gold>0</> disables this functionality.<br>',
                    '\u2022 <blue>Redstone-based ejection</>:',
                    'Decides when box extraction should be allowed, based on redstone signal.',
                    'Use <gold>DISABLED</> in order to keep the box locked until the ejection percent is hit.'
            )))
            addPage(getCraftingRecipe(AntsportationBlocks.BOXER))
        }
        entry(MACHINES_CATEGORY) {
            displayName 'Unboxers'
            icon AntsportationBlocks.UNBOXER
            addPage(new SpotlightPage(AntsportationBlocks.UNBOXER.get(), multiline(
                    '<item>Unboxers</> are special machines which unpack item from boxes.',
                    'Boxes can be inserted from any side, and the extracted items can be extracted from any side as well.',
                    'A box will be locked in the Unboxer until it is fully emptied, at which point, extraction will be allowed.'
            )))
            addPage(boxingStatsPage)
            addPage(getCraftingRecipe(AntsportationBlocks.UNBOXER))
        }
        entry(BLOCKS_CATEGORY){
            displayName 'Ant Hill'
            icon AntsportationBlocks.ANT_HILL
            addPage(new SpotlightPage(AntsportationBlocks.ANT_HILL.get(), multiline(
                    '<item>Ant Hills</> are blocks you can find generating in the world.',
                    'Upon breaking it there is a chance a queen will pop out.',
                    '$(l:antsportation:other/setup_item_transport)You can use Ant hills to set up the input and output for your ants.$()',
                    'Break the Hills with a shovel!'
            )))
        }

        entry(BLOCKS_CATEGORY){
            displayName 'Ant Nest'
            icon AntsportationBlocks.ANT_NEST
            addPage(new SpotlightPage(AntsportationBlocks.ANT_NEST.get(), multiline(
                    '<item>Ant Nests</> are blocks you can find generating in the world.',
                    'Ant Nests are used in combination with the $(l:antsportation:blocks/ant_hill)Ant Hill$() as an $(l:antsportation:other/setup_item_transport)input and/or output point for your ants.$()',
                    'You can input and output on all sides by using something such as a hopper or a pipe from any other mod.'
            )))
        }
        entry(BLOCKS_CATEGORY){
            displayName 'Marker'
            icon AntsportationItems.MARKER
            addPage(new SpotlightPage(AntsportationItems.MARKER.get(), multiline(
                    '<item>Markers</> are used to create a path for the ants to follow.<br>See how this is done in $(l:antsportation:other/setup_item_transport)setup item transport.$()'
            )))
            addPage getCraftingRecipe(AntsportationBlocks.MARKER)
        }
    }

    private static String statsEntry(String name, String config, @Nullable String extraInfo = null) {
        final var str = "\u2022 <gold>$name</>: \$(scfg/$config)"
        if (extraInfo == null)
            return str
        return str + ' ' + extraInfo
    }

    private static PatchouliEntry entry(PatchouliCategory category, String displayName, Supplier<? extends ItemLike> item) {
        return new PatchouliEntry(Antsportation.rlStr(category.fileName), displayName, item.get())
    }

    private static CraftingRecipePage getCraftingRecipe(Supplier<? extends ItemLike> item) {
        return new CraftingRecipePage(Registry.ITEM.getKey(item.get().asItem()).toString())
    }

    private static String multiline(String... strings) {
        final var builder = new StringBuilder()
        strings.eachWithIndex{ String entry, int i ->
            builder.append(entry.replace('\n', '<br>'))
            if (i < (strings.length - 1))
                builder.append('<br>')
        }
        return builder.toString()
    }

    private void entry(PatchouliCategory category, @DelegatesTo(value = EntryBuilder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final var entry = new EntryBuilder(category)
        clos.setDelegate(entry)
        clos.call()
        entries.add(entry)
    }
}
