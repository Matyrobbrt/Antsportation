package com.matyrobbrt.antsportation.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AntWorkerEntity extends PathfinderMob {
    public List<BlockPos> nodeHistory = new ArrayList<>();
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AntWorkerEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<BlockPos> NEXT_MARKER = SynchedEntityData.defineId(AntWorkerEntity.class, EntityDataSerializers.BLOCK_POS);

    public AntWorkerEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }


    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        //TODO: ant walk sound
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PLACE, 0.15F, 1.0F);
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(NEXT_MARKER, null);
    }

    public void setNextMarker(BlockPos pos){
        this.entityData.set(NEXT_MARKER, pos);
    }

    public BlockPos getNextMarker(){
        return this.entityData.get(NEXT_MARKER);
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
            if(level.getGameTime() % 5 == 0){
                if(this.getNextMarker() != null && !nodeHistory.contains(getNextMarker())) {
                    this.navigation.moveTo(getNextMarker().getX(), getNextMarker().getY(), getNextMarker().getZ(), 1);
                    nodeHistory.add(getNextMarker());
                }
            }
        }

    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
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

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if(entityData.get(NEXT_MARKER) != null) {
            pCompound.put("nextMarker", NbtUtils.writeBlockPos(entityData.get(NEXT_MARKER)));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.getCompound("nextMarker") != null) {
            entityData.set(NEXT_MARKER, NbtUtils.readBlockPos(pCompound.getCompound("nextMarker")));
        }
    }
}
