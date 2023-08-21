package com.matyrobbrt.antsportation.onetimejoin;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class OneTimeRewardListener {
    @SubscribeEvent
    static void onJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide())
            return;
        final var pPlayer = event.getEntity();
        event.getEntity().getCapability(OneTimeRewardCap.CAPABILITY).ifPresent(cap -> {
            final var rewards = event.getEntity().level().registryAccess().registryOrThrow(OneTimeReward.RESOURCE_KEY);
            rewards.entrySet().forEach(entry -> {
                if (!cap.getAwardedItems().contains(entry.getKey().location()) && (entry.getValue().requiredMod().isBlank() || ModList.get().isLoaded(entry.getValue().requiredMod()))) {
                    boolean flag = false;
                    for (ItemStack itemstack : entry.getValue().stacks()) {
                        if (pPlayer.addItem(itemstack)) {
                            pPlayer.level().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((pPlayer.getRandom().nextFloat() - pPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            flag = true;
                        } else {
                            ItemEntity itementity = pPlayer.drop(itemstack, false);
                            if (itementity != null) {
                                itementity.setNoPickUpDelay();
                                itementity.setThrower(pPlayer.getUUID());
                            }
                        }
                    }
                    cap.getAwardedItems().add(entry.getKey().location());
                    if (flag) {
                        pPlayer.containerMenu.broadcastChanges();
                    }
                }
            });
        });
    }

    @SubscribeEvent
    static void onAttach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(OneTimeRewardCap.LOCATION, new Provider());
        }
    }

    @SubscribeEvent
    static void onClone(final PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
    }

    private static final class Provider implements ICapabilityProvider, INBTSerializable<ListTag> {
        final OneTimeRewardCap.Impl impl = new OneTimeRewardCap.Impl(new ArrayList<>());
        final LazyOptional<OneTimeRewardCap> lazy = LazyOptional.of(() -> impl);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return OneTimeRewardCap.CAPABILITY.orEmpty(cap, lazy);
        }

        @Override
        public ListTag serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
