package com.matyrobbrt.antsportation.data.patchouli

import com.google.common.hash.Hashing
import com.google.common.hash.HashingOutputStream
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.matyrobbrt.antsportation.util.Utils
import com.matyrobbrt.lib.datagen.patchouli.page.IPatchouliPage
import com.matyrobbrt.lib.datagen.patchouli.page.SpotlightPage
import com.matyrobbrt.lib.datagen.patchouli.page.TextPage
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry
import com.mojang.datafixers.util.Pair
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.stream.Stream

@CompileStatic
class EnUdProvider extends com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider {
    private final com.matyrobbrt.lib.datagen.patchouli.PatchouliProvider sup
    // We don't want to pretty print
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create()

    private final Map<Class<? extends IPatchouliPage>, Closure<IPatchouliPage>> converters = new HashMap<>()
    interface Converter {
        IPatchouliPage apply(IPatchouliPage page);
    }

    EnUdProvider(PatchouliProvider sup, DataGenerator generator) {
        super(generator, sup.modid, "en_ud", sup.bookName)
        this.sup = sup

        registerConverter(SpotlightPage.class, s -> new SpotlightPage(s.item, convertUpsideDown(s.text)))
        registerConverter(TextPage.class, s -> new TextPage(convertUpsideDown(s.title), convertUpsideDown(s.text)))
    }

    @SuppressWarnings("unchecked")
    private <T extends IPatchouliPage> void registerConverter(Class<T> clazz, @ClosureParams(FirstParam.FirstGenericType) Closure<T> op) {
        converters.put(clazz, { op.call((T) it) })
    }

    @Nullable
    @Override
    protected PatchouliBook getBook() {
        return null
    }

    @Override
    void addCategories() {
        resolveCategories().forEach(cat -> categories.add(new PatchouliCategory(convertUpsideDown(cat.name), cat.fileName, convertUpsideDown(cat.description), cat.icon)))
    }

    private Stream<PatchouliCategory> resolveCategories() {
        return Stream.concat(Stream.of(sup.getClass().getDeclaredFields())
                .filter { it.isAnnotationPresent(PatchouliCategoryGen) }
                .filter { it.getType() == PatchouliCategory }
                .map { (PatchouliCategory) it.get(sup) },
                sup.categories.stream())
    }

    @Override
    void addEntries() {
        sup.entries.each { entry ->
            final var newEntry = new PatchouliEntry(entry.category, convertUpsideDown(entry.displayName), entry.icon)
                    .setTurnin(entry.turnin)
            newEntry.fileName = entry.fileName
            entry.pages.collect { convertPage(it) }
                    .each { newEntry.addPage(it) }
            this.entries.add(newEntry)
        }
    }

    private static final List<String> STYLE_TAG = List.of("blue", "gold", "aqua", "item")
    private static final List<Pair<String, String>> STYLES = List.of(Pair.of('$(%s)', '$()'), Pair.of("<%s>", "</>"))

    // TODO smart way of handling <br> and <x></>
    private static String convertUpsideDown(String input) {
        if (input == null)
            return null
//        for (final var tag : STYLE_TAG) {
//            for (final var style : STYLES) {
//                final var in = style.getFirst().formatted(tag)
//                int indexOfLast = 0
//                while (input.indexOf(in) > indexOfLast && input.contains(style.getSecond())) {
//                    indexOfLast = input.indexOf(in)
//                    input = input.replaceFirst(in, style.getSecond())
//                    input = input.replaceFirst(style.getSecond(), in)
//                }
//            }
//        }
        boolean isLessThanSign = false
        int functionEnd = 0
        StringBuilder currentFunction = null
        boolean insideFunction = false
        char[] ud = new char[input.length()]
        for (int i = 0; i < input.length(); i++) {
            if (!insideFunction && currentFunction) {
                final var newStr = currentFunction.toString()
                for (int j = 0; j < newStr.length(); j++) {
                    ud[functionEnd + j] = newStr.charAt(j)
                }
                currentFunction = null
            }
            char c = input.charAt(i)
            final var charIdx = input.length() - 1 - i
            if (insideFunction) { // Keep functions
                currentFunction.append(c)
                if (c == ')' as char || (c == '>' as char && isLessThanSign)) {
                    functionEnd = input.length() - 1 - i
                    insideFunction = false
                }
                continue
            }
            final var isLessThan = c == '<' as char
            if (c === '$' as char || isLessThan && i <= (input.length() - 1)) {
                isLessThanSign = isLessThan
                final var next = input.charAt(i + 1)
                if (isLessThan || next == '(' as char) {
                    currentFunction = new StringBuilder()
                    currentFunction.append(c)
                    insideFunction = true
                    continue
                }
            }
            ud[charIdx] = Utils.toUpsideDown(c)
        }
        return new String(ud)
    }

    IPatchouliPage convertPage(IPatchouliPage page) {
        converters.getOrDefault(page.getClass(), { IPatchouliPage it -> it }).call(page)
    }

    @Override
    void run(@NotNull CachedOutput pCache) throws IOException {
        addEntries()
        Path outputFolder = generator.getOutputFolder()
        for (final entry in entries) {
            final var folder = useResourcePack ? "assets" : "data"
            final var path = outputFolder.resolve("$folder/$modid/patchouli_books/$bookName/$language/entries/${new ResourceLocation(entry.category).getPath()}/${entry.fileName}.json")
            saveEscaped(pCache, path, entry.serialize())
        }

        addCategories()
        for (final category in categories) {
            final var folder = useResourcePack ? "assets" : "data"
            final var path = outputFolder.resolve("$folder/$modid/patchouli_books/$bookName/$language/categories/${category.fileName}.json")
            saveEscaped(pCache, path, category.serialize())
        }
    }

    @SuppressWarnings("deprecation")
    private static void saveEscaped(CachedOutput cache, Path path, JsonElement value) throws IOException {
        var data = GSON.toJson(value)
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data)
        // Escape unicode after the fact so that it's not double escaped by GSON
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha1(), bytearrayoutputstream);
        Writer writer = new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8)
        writer.write(data)
        writer.close()
        cache.writeIfNeeded(path, bytearrayoutputstream.toByteArray(), hashingoutputstream.hash())
    }

    @Override
    @NotNull
    String getName() {
        return 'en_ud Patchouli'
    }
}