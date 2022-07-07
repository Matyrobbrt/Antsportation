package com.matyrobbrt.antsportation.data;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import com.matyrobbrt.antsportation.util.Translations;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

import static com.matyrobbrt.antsportation.Antsportation.rl;

import java.util.function.Supplier;

public class Sounds extends SoundDefinitionsProvider {
    protected Sounds(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Antsportation.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(AntsportationSounds.PACKING, "packing", Translations.SUBTITLE_PACKING);
    }

    @SuppressWarnings("SameParameterValue")
    private void add(Supplier<SoundEvent> sound, String path, Translations translation) {
        add(sound, definition()
                .with(sound(rl(path)))
                .subtitle(translation.key));
    }
}
