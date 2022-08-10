package com.matyrobbrt.antsportation.data.advancement

import com.matyrobbrt.antsportation.Antsportation
import groovy.transform.CompileStatic

@CompileStatic
enum AdvancementLang {
    A_MOD_FOR_ANTS('mod_for_ants', 'What is this? A mod for ants?'),
    A_MOD_FOR_ANTS_DESC('mod_for_ants.desc', 'Get an Ant Jar and start your ant adventures!'),

    GRANT_CANYON('grant_canyon', 'grANT Canyon'),
    GRANT_CANYON_DESC('grant_canyon.desc', 'Get an Ant Hill.'),

    SMALL_INFANTRY('small_infantry', 'Small InfANTry'),
    SMALL_INFANTRY_DESC('small_infantry.desc', 'How can you dare hit an ant.'),

    WRAPPING_UP('wrapping_up', 'Wrapping it up'),
    WRAPPING_UP_DESC('wrapping_up.desc', 'Make a box and start packing items.'),

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