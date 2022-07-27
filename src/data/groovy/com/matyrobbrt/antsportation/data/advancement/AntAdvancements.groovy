package com.matyrobbrt.antsportation.data.advancement

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.item.AntJarItem
import com.matyrobbrt.antsportation.registration.AntsportationItems
import groovy.transform.PackageScope
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.FrameType
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

import java.util.function.BiConsumer
import java.util.function.Consumer

import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.A_MOD_FOR_ANTS
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.A_MOD_FOR_ANTS_DESC

@PackageScope
class AntAdvancements implements AdvancementProvider {
    private final Map<ResourceLocation, LootTable.Builder> rewardsMap = new HashMap<>()

    private Consumer<Advancement> creator
    @Override
    void register(Consumer<Advancement> creator) {
        this.creator = creator
        final var root = advancement('root') {
            display(AntJarItem.withAnt(), A_MOD_FOR_ANTS, A_MOD_FOR_ANTS_DESC, null,
                    FrameType.TASK, true, true, false)
            addCriterion('has_jar', InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item()
                            .hasNbt(AntJarItem.withAnt().getTag())
                            .build()
            ))
            rewards(reward('root') {
                pool {
                    item('markers', AntsportationItems.MARKER.get(), 8)
                    setBonusRolls(UniformGenerator.between(0, 24))
                }
            })
        }
    }

    @Override
    void generateRewards(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        rewardsMap.forEach consumer
    }

    private AdvancementRewards reward(String name, @DelegatesTo(value = LootTable.Builder, strategy = Closure.DELEGATE_FIRST) Closure table) {
        final rl = Antsportation.rl("advancement/ants/$name")
        final tbl = LootTable.lootTable()
        table.resolveStrategy = Closure.DELEGATE_FIRST
        table.delegate = tbl
        table.call(tbl)
        rewardsMap.put rl, tbl
        return AdvancementRewards.Builder.loot(rl).build()
    }
    private Advancement advancement(String name, @DelegatesTo(value = Advancement.Builder, strategy = Closure.DELEGATE_FIRST) Closure clos) {
        final rl = Antsportation.rl("ants/$name")
        final adv = Advancement.Builder.advancement()
        clos.resolveStrategy = Closure.DELEGATE_FIRST
        clos.delegate = adv
        clos.call(adv)
        return adv.save(creator, rl.toString())
    }
}
