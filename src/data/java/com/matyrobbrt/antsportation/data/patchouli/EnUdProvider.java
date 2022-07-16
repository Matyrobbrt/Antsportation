package com.matyrobbrt.antsportation.data.patchouli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.matyrobbrt.antsportation.data.client.Lang;
import com.matyrobbrt.antsportation.util.Utils;
import com.matyrobbrt.lib.datagen.patchouli.page.IPatchouliPage;
import com.matyrobbrt.lib.datagen.patchouli.page.SpotlightPage;
import com.matyrobbrt.lib.datagen.patchouli.page.TextPage;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry;
import com.mojang.datafixers.util.Pair;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class EnUdProvider extends com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider {
    private final com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider sup;
    // We don't want to pretty print
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final Map<Class<? extends IPatchouliPage>, UnaryOperator<IPatchouliPage>> converters = new HashMap<>();

    EnUdProvider(PatchouliProvider sup, DataGenerator generator) {
        super(generator, sup.modid, "en_ud", sup.bookName);
        this.sup = sup;

        registerConverter(SpotlightPage.class, s -> new SpotlightPage(s.item, convertUpsideDown(s.text)));
        registerConverter(TextPage.class, s -> new TextPage(convertUpsideDown(s.title), convertUpsideDown(s.text)));
    }

    @SuppressWarnings("unchecked")
    private <T extends IPatchouliPage> void registerConverter(Class<T> clazz, UnaryOperator<T> op) {
        converters.put(clazz, page -> op.apply((T) page));
    }

    @Nullable
    @Override
    protected PatchouliBook getBook() {
        return null;
    }

    @Override
    public void addCategories() {
        resolveCategories().forEach(cat -> categories.add(new PatchouliCategory(convertUpsideDown(cat.name), cat.fileName, convertUpsideDown(cat.description), cat.icon)));
    }

    private Stream<PatchouliCategory> resolveCategories() {
        return Stream.concat(Stream.of(sup.getClass().getDeclaredFields())
                        .filter(f -> f.isAnnotationPresent(PatchouliCategoryGen.class))
                        .filter(f -> f.getType() == PatchouliCategory.class)
                        .map(LamdbaExceptionUtils.rethrowFunction(f -> (PatchouliCategory) f.get(sup))),
                sup.categories.stream());
    }

    @Override
    public void addEntries() {
        sup.entries.forEach(entry -> {
            final var newEntry = new PatchouliEntry(entry.category, convertUpsideDown(entry.displayName), entry.icon)
                    .setTurnin(entry.turnin);
            newEntry.fileName = entry.fileName;
            entry.pages.stream()
                    .map(this::convertPage)
                    .forEach(newEntry::addPage);
            this.entries.add(newEntry);
        });
    }

    private static final List<String> STYLE_TAG = List.of("blue","gold", "aqua", "item");
    private static final List<Pair<String, String>> STYLES = List.of(Pair.of("$(%s)", "$()"), Pair.of("<%s>", "</>"));

    // TODO smart way of handling <br> and <x></>
    private static String convertUpsideDown(String input) {
        if (input == null)
            return null;
//        for (final var tag : STYLE_TAG) {
//            for (final var style : STYLES) {
//                final var in = style.getFirst().formatted(tag);
//                int indexOfLast = 0;
//                while (input.indexOf(in) > indexOfLast && input.contains(style.getSecond())) {
//                    indexOfLast = input.indexOf(in);
//                    input = input.replaceFirst(in, style.getSecond());
//                    input = input.replaceFirst(style.getSecond(), in);
//                }
//            }
//        }
        boolean isLessThanSign = false;
        int functionEnd = 0;
        StringBuilder currentFunction = null;
        boolean insideFunction = false;
        char[] ud = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            if (!insideFunction && currentFunction != null) {
                final var newStr = currentFunction.toString();
                for (int j = 0; j < newStr.length(); j++) {
                    ud[functionEnd + j] = newStr.charAt(j);
                }
                currentFunction = null;
            }
            char c = input.charAt(i);
            final var charIdx = input.length() - 1 - i;
            if (insideFunction) { // Keep functions
                currentFunction.append(c);
                if (c == ')' || (c == '>' && isLessThanSign)) {
                    functionEnd = input.length() - 1 - i;
                    insideFunction = false;
                }
                continue;
            }
            final var isLessThan = c == '<';
            if (c == '$' || isLessThan && i <= (input.length() - 1)) {
                isLessThanSign = isLessThan;
                final var next = input.charAt(i + 1);
                if (isLessThan || next == '(') {
                    currentFunction = new StringBuilder();
                    currentFunction.append(c);
                    insideFunction = true;
                    continue;
                }
            }
            ud[charIdx] = Utils.toUpsideDown(c);
        }
        return new String(ud);
    }

    private IPatchouliPage convertPage(IPatchouliPage page) {
        return converters.computeIfAbsent(page.getClass(), k -> e -> e).apply(page);
    }

    @Override
    public void run(@NotNull HashCache pCache) throws IOException {
        addEntries();
        Path outputFolder = generator.getOutputFolder();
        for (final var entry : entries) {
            final var folder = useResourcePack ? "assets" : "data";
            Path path = outputFolder.resolve(folder + "/" + modid + "/patchouli_books/" + bookName + "/" + language
                    + "/entries/" + new ResourceLocation(entry.category).getPath() + "/" + entry.fileName + ".json");
            saveEscaped(pCache, path, entry.serialize());
        }

        addCategories();
        for (final var category : categories) {
            final var folder = useResourcePack ? "assets" : "data";
            Path path = outputFolder.resolve(folder + "/" + modid + "/patchouli_books/" + bookName + "/" + language
                    + "/categories/" + category.fileName + ".json");
            saveEscaped(pCache, path, category.serialize());
        }
    }

    @SuppressWarnings("deprecation")
    private static void saveEscaped(HashCache cache, Path path, JsonElement value) throws IOException {
        var data = GSON.toJson(value);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)) {
            Files.createDirectories(path.getParent());

            try (final var writer = Files.newBufferedWriter(path)) {
                writer.write(data);
            }
        }

        cache.putNew(path, hash);
    }

    @Override
    public @NotNull String getName() {
        return "en_ud Patchouli";
    }
}