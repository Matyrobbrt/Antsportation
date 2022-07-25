package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAntEntity extends PathfinderMob {
    protected BaseAntEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return AntsportationSounds.ANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AntsportationSounds.ANT_DEATH.get();
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }
}
