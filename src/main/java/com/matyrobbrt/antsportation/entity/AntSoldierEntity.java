package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AntSoldierEntity extends PathfinderMob {

    public AntSoldierEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
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
                return random.nextBoolean() && pLevel.getBlockState(pPos).is(AntsportationBlocks.ANT_HILL.get());
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D, 1F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers().setAlertOthers(AntQueenEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 5, false, false, (p_28879_) -> p_28879_ instanceof Player));
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(AntsportationSounds.ANT_WALK.get(), 0.12f, 1);
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
        return 0.40f;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        if (super.doHurtTarget(pEntity)) {
            playSound(AntsportationSounds.ANT_ATTACK.get(), 0.10f, 1);
            return true;
        }
        return false;
    }
}
