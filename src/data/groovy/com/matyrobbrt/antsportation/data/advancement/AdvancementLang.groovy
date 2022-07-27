package com.matyrobbrt.antsportation.data.advancement

import com.matyrobbrt.antsportation.Antsportation
import groovy.transform.CompileStatic

@CompileStatic
enum AdvancementLang {
    A_MOD_FOR_ANTS('mod_for_ants', 'What is this? A mod for ants?'),
    A_MOD_FOR_ANTS_DESC('mod_for_ants.desc', 'Get an Ant Jar and start your ant adventures!'),


    ;
    public final String key
    public final String englishText
    AdvancementLang(final String key, String english) {
        this.key = "advancement.${Antsportation.MOD_ID}.$key"
        this.englishText = english
    }

    @Override
    String toString() {
        key
    }
}