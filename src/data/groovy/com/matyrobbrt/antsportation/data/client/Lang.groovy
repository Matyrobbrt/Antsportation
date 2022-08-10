package com.matyrobbrt.antsportation.data.client

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.data.advancement.AdvancementLang
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationEntities
import com.matyrobbrt.antsportation.registration.AntsportationItems
import com.matyrobbrt.antsportation.util.Translations
import com.matyrobbrt.antsportation.util.Utils
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider
import org.jetbrains.annotations.NotNull

import javax.annotation.ParametersAreNonnullByDefault
import java.util.stream.Stream

@ParametersAreNonnullByDefault
class Lang extends LanguageProvider {
    public final AccessibleLanguageProvider enUd

    Lang(DataGenerator gen) {
        super(gen, Antsportation.MOD_ID, "en_us")
        enUd = new AccessibleLanguageProvider(gen, Antsportation.MOD_ID, "en_ud")
    }

    private static class AccessibleLanguageProvider extends LanguageProvider {

        AccessibleLanguageProvider(DataGenerator gen, String modid, String locale) {
            super(gen, modid, locale)
        }

        @Override
        void add(@NotNull String key, @NotNull String value) {
            super.add(key, value)
        }

        @Override
        protected void addTranslations() {}
    }

    @Override
    protected void addTranslations() {
        AntsportationItems.ITEMS.getEntries().forEach(item -> {
            final var name = String.join(" ", Stream.of(item.getId().getPath().split("_")).map(Lang::capitalize).toList())
            addItem(item, name)
        })

        for (final value in Translations.values()) {
            add(value.key, value.englishTranslation)
        }
        for (final value in AdvancementLang.values()) {
            add(value.key, value.englishText)
        }

        for (final tier in BoxItem.BoxTier.values()) {
            add(tier.getTranslationKey(), capitalize(tier.name().toLowerCase(Locale.ROOT)))
        }

        AntsportationEntities.ENTITIES.getEntries().forEach(entity -> {
            final var name = String.join(" ", Stream.of(entity.getId().getPath().split("_")).map(Lang::capitalize).toList())
            add(entity.get(), name)
        })

        add AntsportationEntities.HILL_ANT_SOLDIER.get(), 'Ant Soldier'

        add("itemGroup.${Antsportation.MOD_ID}", "Antsportation")
    }

    static String capitalize(String str) {
        final var firstChar = str.charAt(0)
        return String.valueOf(firstChar).toUpperCase(Locale.ROOT) + str.substring(1)
    }

    void add(String key, String value) {
        try {
            super.add(key, value)
            enUd.add(key, Utils.toUpsideDown(value))
        } catch (IllegalStateException ignored) {}
    }

}
