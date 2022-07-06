package com.matyrobbrt.antsportation.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AntWorkerEntity extends PathfinderMob {

    public AntWorkerEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
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
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers().setAlertOthers(AntQueenEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 5, false, false, (p_28879_) -> p_28879_ instanceof Player));
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        //TODO: ant walk sound
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PLACE, 0.15F, 1.0F);
    }


    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        //TODO: ant hurt sound
        return SoundEvents.DRIPSTONE_BLOCK_FALL;
    }

    protected SoundEvent getDeathSound() {
        //TODO: ant death sound
        return SoundEvents.SILVERFISH_DEATH;
    }

    protected float getSoundVolume() {
        return 0.1F;
    }
}
