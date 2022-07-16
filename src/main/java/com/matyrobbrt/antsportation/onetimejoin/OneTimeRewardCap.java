package com.matyrobbrt.antsportation.onetimejoin;

import com.matyrobbrt.antsportation.Antsportation;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface OneTimeRewardCap {

    Capability<OneTimeRewardCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    ResourceLocation LOCATION = Antsportation.rl("one_time_rewards");

    List<ResourceLocation> getAwardedItems();

    record Impl(List<ResourceLocation> list) implements OneTimeRewardCap, INBTSerializable<ListTag> {

        @Override
        public List<ResourceLocation> getAwardedItems() {
            return list;
        }

        @Override
        public ListTag serializeNBT() {
            final var tag = new ListTag();
            list.forEach(r -> tag.add(StringTag.valueOf(r.toString())));
            return tag;
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            list.clear();
            nbt.forEach(n -> list.add(new ResourceLocation(n.getAsString())));
        }
    }
}
