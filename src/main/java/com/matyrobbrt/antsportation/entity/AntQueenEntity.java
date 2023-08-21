package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.block.entity.AntHillBE;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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

public class AntQueenEntity extends BaseAntEntity implements NeutralMob {
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    public static final int REINFORCEMENT_DELAY = 300;
    private int ticksSinceLastSummon = REINFORCEMENT_DELAY;

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
                return pLevel.getBlockEntity(pPos) instanceof AntHillBE hill && !hill.hasQueen;
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
    public void tick() {
        super.tick();
        ticksSinceLastSummon++;
        if (ticksSinceLastSummon >= REINFORCEMENT_DELAY) {
            ticksSinceLastSummon = REINFORCEMENT_DELAY;
        }
    }

    private static final UUID HURT_BONUS_UID = UUID.fromString("fd0942e6-ed28-4f99-a36b-37c5a6ad50c5");
    private static final AttributeModifier HURT_MODIFIER = new AttributeModifier(HURT_BONUS_UID, "Hurt bonus", 10, AttributeModifier.Operation.ADDITION);

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity pLivingEntity) {
        final var soliderTarget = getLastHurtByMob() == null ? pLivingEntity : getLastHurtByMob();
        super.setLastHurtByMob(pLivingEntity);

        if (ticksSinceLastSummon >= REINFORCEMENT_DELAY && !level().isClientSide()) {
            final var attr = getAttribute(Attributes.MAX_HEALTH);
            // noinspection ConstantConditions
            if (!attr.hasModifier(HURT_MODIFIER))
                attr.addPermanentModifier(HURT_MODIFIER);

            heal(5);

            for (int i = 0; i < 4; ++i) {
                final var soldier = AntSoldierEntity.spawnReinforcement(level(), this.blockPosition());
                soldier.setTarget(soliderTarget);
            }
            ticksSinceLastSummon = 0;
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("ticksSinceLastSummon", ticksSinceLastSummon);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        ticksSinceLastSummon = pCompound.getInt("ticksSinceLastSummon");
    }

    @Override
    protected float getSoundVolume() {
        return 0.30F;
    }
}