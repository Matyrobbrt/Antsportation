package com.matyrobbrt.antsportation.onetimejoin;

import com.matyrobbrt.antsportation.Antsportation;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record OneTimeReward(String requiredMod, List<ItemStack> stacks) {
    public static final Codec<OneTimeReward> CODEC = new Codec<>() {
        final Codec<List<ItemStack>> item = ItemStack.CODEC.listOf();
        @Override
        public <T> DataResult<Pair<OneTimeReward, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> {
                var val = map.get("requiredMod");
                if (val == null)
                    val = ops.createString("");
                return ops.getStringValue(val).flatMap(mod -> {
                    if (!mod.isBlank() && !ModList.get().isLoaded(mod)) {
                        return DataResult.success(new OneTimeReward(mod, List.of()));
                    } else {
                        return item.decode(ops, map.get("items")).map(p -> new OneTimeReward(mod, p.getFirst()));
                    }
                });
            }).map(e -> Pair.of(e, ops.empty()));
        }

        @Override
        public <T> DataResult<T> encode(OneTimeReward input, DynamicOps<T> ops, T prefix) {
            return ops.mapBuilder()
                    .add(ops.createString("requiredMod"), ops.createString(input.requiredMod))
                    .add("items", input.stacks, item)
                    .build(prefix);
        }
    };
    public static final ResourceKey<Registry<OneTimeReward>> RESOURCE_KEY = ResourceKey.createRegistryKey(Antsportation
            .rl("one_time_reward"));
    public static final DeferredRegister<OneTimeReward> ONE_TIME_REWARDS = DeferredRegister.create(RESOURCE_KEY, Antsportation.MOD_ID);

    public OneTimeReward(String requiredMod, ItemStack... stacks) {
        this(requiredMod, List.of(stacks));
    }

    @Override
    public List<ItemStack> stacks() {
        return stacks.stream().map(ItemStack::copy).collect(Collectors.toCollection(ArrayList::new));
    }

}
