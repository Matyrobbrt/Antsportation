package com.matyrobbrt.antsportation.entity;

import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class AntWorkerEntity extends BaseAntEntity {
    public List<BlockPos> nodeHistory = new ArrayList<>();
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AntWorkerEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<BlockPos> NEXT_MARKER = SynchedEntityData.defineId(AntWorkerEntity.class, EntityDataSerializers.BLOCK_POS);

    public AntWorkerEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (entityData.get(NEXT_MARKER) != null) {
            pCompound.put("nextMarker", NbtUtils.writeBlockPos(entityData.get(NEXT_MARKER)));
        }
        ListTag listtag = new ListTag();
        nodeHistory.forEach((node) -> {
            if (node != null) {
                listtag.add(NbtUtils.writeBlockPos(node));
            }
        });
        pCompound.put("nodeHistory", listtag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.getCompound("nextMarker") != null) {
            entityData.set(NEXT_MARKER, NbtUtils.readBlockPos(pCompound.getCompound("nextMarker")));
        }
        nodeHistory = pCompound.getList("nodeHistory", 10).stream().map(((tag) -> (CompoundTag) tag)).map(NbtUtils::readBlockPos).collect(Collectors.toList());
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(AntsportationSounds.ANT_WALK.get(), 0.08f, 1);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        final var nav = new WallClimberNavigation(this, pLevel);
        nav.setCanOpenDoors(true);
        return nav;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(NEXT_MARKER, null);
    }

    public void setNextMarker(BlockPos pos) {
        this.entityData.set(NEXT_MARKER, pos);
    }

    public BlockPos getNextMarker() {
        return this.entityData.get(NEXT_MARKER);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
            if (level.getGameTime() % 5 == 0) {
                if (this.getNextMarker() != null) {
                    this.navigation.moveTo(getNextMarker().getX(), getNextMarker().getY(), getNextMarker().getZ(), 1);
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
        setPersistenceRequired();
        setGuaranteedDrop(pSlot);

        public boolean isClimbing () {
            return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
        }

        @Override
        public boolean onClimbable () {
            return this.isClimbing();
        }

        public void setClimbing ( boolean pClimbing){
            byte b0 = this.entityData.get(DATA_FLAGS_ID);
            if (pClimbing) {
                b0 = (byte) (b0 | 1);
            } else {
                b0 = (byte) (b0 & -2);
            }

            this.entityData.set(DATA_FLAGS_ID, b0);
        }

        @Override
        protected float getSoundVolume () {
            return 0.25f;
        }


    }
}
