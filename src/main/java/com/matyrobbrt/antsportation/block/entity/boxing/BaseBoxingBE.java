package com.matyrobbrt.antsportation.block.entity.boxing;

import com.matyrobbrt.antsportation.compat.top.StackWithProgressElement;
import com.matyrobbrt.antsportation.compat.top.TOPContext;
import com.matyrobbrt.antsportation.compat.top.TOPInfoDriver;
import com.matyrobbrt.antsportation.item.BoxItem;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.registration.AntsportationSounds;
import com.matyrobbrt.antsportation.util.cap.EnergyStorage;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseBoxingBE extends BlockEntity implements TOPInfoDriver {
    public BaseBoxingBE(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public final EnergyStorage energy = new EnergyStorage(ServerConfig.CONFIG.boxing().energyCapacity().get(), 10 * (ServerConfig.CONFIG.boxing().baseUsedEnergy().get() +
            AntsportationItems.SPEED_UPGRADE.get().getDefaultInstance().getMaxStackSize() * ServerConfig.CONFIG.boxing().upgradeEnergyUsage().get()), 0) {
        @Override
        public void onChanged() {
            BaseBoxingBE.this.setChanged();
        }
    };

    public int maxProgress = ServerConfig.CONFIG.boxing().baseNeededTicks().get();
    public int progress = 0;
    protected int energyUsage = ServerConfig.CONFIG.boxing().baseUsedEnergy().get();

    public final ItemStackHandler box = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof BoxItem;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            BaseBoxingBE.this.setChanged();
        }
    };
    public final ItemStackHandler upgrades = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(AntsportationItems.SPEED_UPGRADE.get());
        }

        @Override
        protected void onContentsChanged(int slot) {
            onLoad();
            BaseBoxingBE.this.setChanged();
        }

        @Override
        protected void onLoad() {
            final var stack = stacks.get(0);
            maxProgress = ServerConfig.getBoxing(ServerConfig.Boxing::baseNeededTicks) - stack.getCount() * ServerConfig.getBoxing(ServerConfig.Boxing::upgradeReduction);
            energyUsage = ServerConfig.getBoxing(ServerConfig.Boxing::baseUsedEnergy) + stack.getCount() * ServerConfig.getBoxing(ServerConfig.Boxing::upgradeEnergyUsage);
        }
    };

    public abstract ItemStackHandler getInventory();

    protected void manipulateProgress(boolean decrease) {
        if (decrease) {
            if (progress > 0) {
                progress -= 1;
                setChanged();
            }
        } else {
            if ((energy.getEnergyStored() >= energyUsage || !ServerConfig.CONFIG.boxing().useEnergy().get()) && progress < maxProgress) {
                progress += 1;
                energy.extractInternal(energyUsage);
                setChanged();
            }
        }
    }

    protected void whenProgressMade() {
        progress = 0;
        setChanged();
        level.playSound(null, worldPosition, AntsportationSounds.PACKING.get(), SoundSource.BLOCKS, 0.8f, 1f);
    }

    private final LazyOptional<EnergyStorage> energyLazy = LazyOptional.of(() -> energy);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && ServerConfig.CONFIG.boxing().useEnergy().get()) {
            return energyLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        upgrades.deserializeNBT(nbt.getCompound("upgrades"));
        box.deserializeNBT(nbt.getCompound("box"));
        getInventory().deserializeNBT(nbt.getCompound("inventory"));
        energy.deserializeNBT(nbt.get("energy"));

        progress = nbt.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("upgrades", upgrades.serializeNBT());
        pTag.put("box", box.serializeNBT());
        pTag.put("inventory", getInventory().serializeNBT());
        pTag.put("energy", energy.serializeNBT());

        pTag.putInt("progress", progress);
    }

    public void dropContents() {
        dropContents(getInventory());
        dropContents(box);
        dropContents(upgrades);
    }

    private void dropContents(IItemHandler handler) {
        if (level == null)
            return;
        for (int i = 0; i < handler.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), handler.getStackInSlot(i));
        }
    }

    public int getProgressionScaled() {
        return progress != 0 && maxProgress != 0
                ? progress * 24 / maxProgress
                : 0;
    }

    @Override
    public void addInfo(TOPContext context) {
        final var box = this.box.getStackInSlot(0);
        if (!box.isEmpty()) {
            final var direction = isReversed() ? StackWithProgressElement.Direction.RIGHT_TO_LEFT : StackWithProgressElement.Direction.LEFT_TO_RIGHT;
            context.addStackWithProgressElement(box, getProgressionScaled(), direction);
        }
    }

    protected boolean isReversed() {
        return false;
    }
}
