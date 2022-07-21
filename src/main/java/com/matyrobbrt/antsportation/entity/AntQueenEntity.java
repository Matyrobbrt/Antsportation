package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AntQueenEntity extends PathfinderMob implements NeutralMob {
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);


    public AntQueenEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.4f).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.3f));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.addGoal(6, new MoveToBlockGoal(this, 1, 16) {
            @Override
            protected boolean isValidTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
                return pLevel.getBlockState(pPos).is(AntsportationBlocks.ANT_HILL.get());
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers().setAlertOthers(AntSoldierEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Silverfish.class, 5, false, false, (p_28879_) -> p_28879_ instanceof Silverfish));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Endermite.class, 5, false, false, (p_28879_) -> p_28879_ instanceof Endermite));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.remainingPersistentAngerTime = pTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(AntsportationSounds.ANT_WALK.get(), 0.15f, 1);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        if (super.doHurtTarget(pEntity)) {
            playSound(AntsportationSounds.ANT_ATTACK.get(), 0.12f, 1);
            return true;
        }
        return false;
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
    protected float getSoundVolume() {
        return 0.30F;
    }
}