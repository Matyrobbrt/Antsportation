package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class Lang extends LanguageProvider {
    public final AccessibleLanguageProvider enUd;

    public Lang(DataGenerator gen) {
        super(gen, Antsportation.MOD_ID, "en_us");
        enUd = new AccessibleLanguageProvider(gen, Antsportation.MOD_ID, "en_ud");
    }

    private static class AccessibleLanguageProvider extends LanguageProvider {

        public AccessibleLanguageProvider(DataGenerator gen, String modid, String locale) {
            super(gen, modid, locale);
        }

        @Override
        public void add(@NotNull String key, @NotNull String value) {
            super.add(key, value);
        }

        @Override
        protected void addTranslations() {}
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

        AntsportationEntities.ENTITIES.getEntries().forEach(entity -> {
            final var name = String.join(" ", Stream.of(entity.getId().getPath().split("_")).map(Lang::capitalize).toList());
            add(entity.get(), name);
        });

        add(((TranslatableComponent) Antsportation.TAB.getDisplayName()).getKey(), "Antsportation");
    }

    @Override
    public void run(@NotNull HashCache cache) throws IOException {
        super.run(cache);
        enUd.run(cache);
    }

    private static String capitalize(String str) {
        final var firstChar = str.charAt(0);
        return String.valueOf(firstChar).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    public void add(String key, String value) {
        super.add(key, value);
        enUd.add(key, toUpsideDown(value));
    }

    // Automatic en_ud generation
    // Inspired from https://github.com/Tropicraft/Tropicraft/blob/a600e6bce7f1d131eb2a7d1e6fa36e1718d2014f/src/main/java/net/tropicraft/core/client/data/TropicraftLangProvider.java#L526-L567

    private static final String NORMAL_CHARS =
            /* lowercase */ "abcdefghijklmn\u00F1opqrstuvwxyz" +
            /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            /*  numbers  */ "0123456789" +
            /*  special  */ "_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS =
            /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
            /* uppercase */ "\u2C6F\u15FA\u0186\u15E1\u018E\u2132\u2141HI\u017F\u029E\uA780WNO\u0500\u1F49\u1D1AS\u27D8\u2229\u039BMX\u028EZ" +
            /*  numbers  */ "0\u0196\u1105\u0190\u3123\u03DB9\u312586" +
            /*  special  */ "\u203E'\u061B\u02D9\u00BF\u00A1/\\,";

    private String toUpsideDown(String normal) {
        char[] ud = new char[normal.length()];
        for (int i = 0; i < normal.length(); i++) {
            char c = normal.charAt(i);
            if (c == '%') {
                StringBuilder fmtArg = new StringBuilder();
                while (Character.isDigit(c) || c == '%' || c == '$' || c == 's' || c == 'd') { // TODO this is a bit lazy
                    fmtArg.append(c);
                    i++;
                    c = i == normal.length() ? 0 : normal.charAt(i);
                }
                i--;
                for (int j = 0; j < fmtArg.length(); j++) {
                    ud[normal.length() - 1 - i + j] = fmtArg.charAt(j);
                }
                continue;
            }
            int lookup = NORMAL_CHARS.indexOf(c);
            if (lookup >= 0) {
                c = UPSIDE_DOWN_CHARS.charAt(lookup);
            }
            ud[normal.length() - 1 - i] = c;
        }
        return new String(ud);
    }
}
