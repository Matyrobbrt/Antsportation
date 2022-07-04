package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;
import java.util.stream.Stream;

public class Lang extends LanguageProvider {
    public Lang(DataGenerator gen) {
        super(gen, Antsportation.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        AntsportationItems.ITEMS.getEntries().forEach(item -> {
            final var name = String.join(" ", Stream.of(item.getId().getPath().split("_")).map(Lang::capitalize).toList());
            addItem(item, name);
        });
        for (final Translations value : Translations.values()) {
            add(value.key, value.englishTranslation);
        }
        for (final var tier : BoxItem.BoxTier.values()) {
            add(tier.getTranslationKey(), capitalize(tier.name().toLowerCase(Locale.ROOT)));
        }
    }

    private static String capitalize(String str) {
        final var firstChar = str.charAt(0);
        return String.valueOf(firstChar).toUpperCase(Locale.ROOT) + str.substring(1);
    }
}
