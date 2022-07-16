package com.matyrobbrt.antsportation.data.patchouli

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry
import groovy.transform.CompileStatic
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

import java.util.function.Supplier

@CompileStatic
class EntryBuilder extends PatchouliEntry {
    EntryBuilder(PatchouliCategory category) {
        super(Antsportation.rlStr(category.fileName), '', Items.AIR)
    }

    void displayName(String displayName) {
        this.displayName = displayName
    }

    void setDisplayName(String displayName) {
        this.@displayName = displayName
        fileName = displayName.toLowerCase().replace(' ', '_')
    }

    void icon(Supplier<? extends ItemLike> icon) {
        this.icon = icon.get().asItem()
    }
}
