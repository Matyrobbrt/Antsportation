package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HillAntSoldierEntity extends AntSoldierEntity {
    public HillAntSoldierEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected Vec3 targetPos;

    public void setTargetPos(BlockPos pos) {
        this.targetPos = new Vec3(pos.getX(), pos.getY() - 0.5D, pos.getZ());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new RandomStrollGoal(this, 1, 60) {
            @Override
            protected Vec3 getPosition() {
                return targetPos;
            }
        });
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("targetPos", Tag.TAG_COMPOUND))
            targetPos = Utils.readVec3(pCompound.getCompound("targetPos"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (targetPos != null)
            pCompound.put("targetPos", Utils.writeVec3(targetPos));
    }

}
