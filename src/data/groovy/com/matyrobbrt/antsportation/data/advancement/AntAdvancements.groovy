package com.matyrobbrt.antsportation.data.advancement

import com.matyrobbrt.antsportation.Antsportation
import com.matyrobbrt.antsportation.item.AntJarItem
import com.matyrobbrt.antsportation.item.BoxItem
import com.matyrobbrt.antsportation.registration.AntsportationBlocks
import com.matyrobbrt.antsportation.registration.AntsportationItems
import com.matyrobbrt.antsportation.registration.AntsportationTags
import groovy.transform.PackageScope
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.FrameType
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

import java.util.function.BiConsumer
import java.util.function.Consumer

import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.A_MOD_FOR_ANTS
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.A_MOD_FOR_ANTS_DESC
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.GRANT_CANYON
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.GRANT_CANYON_DESC
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.SMALL_INFANTRY
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.SMALL_INFANTRY_DESC
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.WRAPPING_UP
import static com.matyrobbrt.antsportation.data.advancement.AdvancementLang.WRAPPING_UP_DESC

@PackageScope
class AntAdvancements implements AdvancementProvider {
    private final Map<ResourceLocation, LootTable.Builder> rewardsMap = new HashMap<>()

    private Consumer<Advancement> creator
    @Override
    void register(Consumer<Advancement> creator) {
        this.creator = creator
        final var root = advancement('root') {
            display(AntJarItem.withAnt(), A_MOD_FOR_ANTS, A_MOD_FOR_ANTS_DESC, new ResourceLocation('textures/block/dirt.png'),
                    FrameType.TASK, true, true, false)
            addCriterion('has_jar', hasItems(AntJarItem.withAnt()))
            rewards(reward('root') {
                pool {
                    item('markers', AntsportationItems.MARKER.get(), 8)
                    setBonusRolls(UniformGenerator.between(0, 24))
                }
            })
        }

        final var grantCanyon = advancement('grant_canyon') {
            display(AntsportationBlocks.ANT_HILL.get(), GRANT_CANYON, GRANT_CANYON_DESC, null,
                    FrameType.TASK, true, true, false)
            parent(root)
            addCriterion('has_hill', hasItems(AntsportationBlocks.ANT_HILL.get()))
            rewards(reward('grant_canyon') {
                pool { item('hill', AntsportationBlocks.ANT_HILL.get(), 1) }
                pool { item('nests', AntsportationBlocks.ANT_NEST.get(), 2) }
            })
        }

        advancement('small_infantry') {
            display(AntsportationItems.ANT_SOLDIER_SPAWN_EGG.get(), SMALL_INFANTRY, SMALL_INFANTRY_DESC, null,
                    FrameType.TASK, true, true, false)
            parent(grantCanyon)
            addCriterion('hit_ant', PlayerHurtEntityTrigger.TriggerInstance
                    .playerHurtEntity(EntityPredicate.Builder.entity()
                        .of(AntsportationTags.Entities.ANTS)
                        .build()))
        }

        final var wrappingUp = advancement('wrapping_it_up') {
            display(BoxItem.BoxTier.BASIC, WRAPPING_UP, WRAPPING_UP_DESC, null,
                    FrameType.TASK, true, true, false)
            parent(root)
            addCriterion('has_box', InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(AntsportationTags.Items.BOXES).build()
            ))
            rewards(reward('wrapping_it_up') {
                pool { item('box', BoxItem.BoxTier.ADVANCED, 1) }
            })
        }
    }

    @Override
    void generateRewards(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        rewardsMap.forEach consumer
    }

    private static CriterionTriggerInstance hasItems(ItemStack... items) {
        final List<ItemPredicate> predicates = []
        items.each {
            final builder = ItemPredicate.Builder.item()
                .of(it.item)

            if (it.tag)
                builder.hasNbt(it.tag)
            if (it.count > 1)
                builder.withCount(MinMaxBounds.Ints.atLeast(it.count))

            predicates.add builder.build()
        }
        return InventoryChangeTrigger.TriggerInstance.hasItems(predicates.array())
    }

    private static CriterionTriggerInstance hasItems(ItemLike... items) {
        final List<ItemPredicate> predicates = []
        items.each {
            final builder = ItemPredicate.Builder.item()
                .of(it)
            predicates.add builder.build()
        }
        return InventoryChangeTrigger.TriggerInstance.hasItems(predicates.array())
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
