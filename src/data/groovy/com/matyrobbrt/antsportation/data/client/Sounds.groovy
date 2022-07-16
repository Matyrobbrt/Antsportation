package com.matyrobbrt.antsportation.data.client

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.registration.AntsportationSounds
import com.matyrobbrt.antsportation.util.Translations
import net.minecraft.data.DataGenerator
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.SoundDefinitionsProvider

import java.util.function.Supplier

import static com.matyrobbrt.antsportation.Antsportation.rl

class Sounds extends SoundDefinitionsProvider {
    Sounds(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Antsportation.MOD_ID, helper)
    }

    @Override
    void registerSounds() {
        add(AntsportationSounds.PACKING, 'packing', Translations.SUBTITLE_PACKING)
    }

    @SuppressWarnings('SameParameterValue')
    private void add(Supplier<SoundEvent> sound, String path, Translations translation) {
        add(sound, definition()
                .with(this.sound(rl(path)))
                .subtitle(translation.key))
    }
}
