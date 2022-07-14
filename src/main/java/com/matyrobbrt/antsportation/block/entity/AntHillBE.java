package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class AntHillBE extends BlockEntity {
    public final AntHillBE.Inventory inventory = new Inventory();
    private static final int SPAWNRATE = 100;
    public boolean hasQueen = false;


    public AntHillBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_HILL_BE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        nbt.getBoolean("hasQueen");
    }

    //Temporary functionality TODO: do the ant spawning when we get that far
    public void tick(){
        if(level != null && hasQueen && level.getGameTime()%SPAWNRATE==0 && !level.isClientSide()){
            AntSoldierEntity antSoldier = new AntSoldierEntity(AntsportationEntities.ANT_SOLDIER.get(), level);
            antSoldier.setPos(getBlockPos().getX()+0.5, getBlockPos().getY()+1, getBlockPos().getZ()+0.5);
            level.addFreshEntity(antSoldier);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", inventory.serializeNBT());
        pTag.putBoolean("hasQueen", hasQueen);
    }

    private void dropContents(IItemHandler handler) {
        if (level == null)
            return;
        for (int i = 0; i < handler.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), handler.getStackInSlot(i));
        }
    }

    public void dropContents() {
        dropContents(inventory);
    }

    class Inventory extends ItemStackHandler {
        public Inventory() {
            super(10);
        }

        protected NonNullList<ItemStack> getStacks() {
            return stacks;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            AntHillBE.this.setChanged();
        }
    }
}
