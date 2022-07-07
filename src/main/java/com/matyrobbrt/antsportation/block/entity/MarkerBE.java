package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MarkerBE extends BlockEntity {

    private int sugarAmount = 0;
    private DyeColor color = DyeColor.WHITE;
    private static final String SUGAR_NBT_KEY = "sugar_amount";
    private static final String COLOR_NBT_KEY = "dye_color";
    public MarkerBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.MARKER_BE.get(), pWorldPosition, pBlockState);
    }

    public int getSugarAmount() {
        return sugarAmount;
    }

    public boolean increaseSugarAmount() {
        if (sugarAmount >= 10) {
            return false;
        }
        sugarAmount += 1;
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        return true;
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        sugarAmount = nbt.getInt(SUGAR_NBT_KEY);
        color = DyeColor.byName(nbt.getString(COLOR_NBT_KEY), DyeColor.WHITE);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt(SUGAR_NBT_KEY, sugarAmount);
        pTag.putString(COLOR_NBT_KEY, color.getName());
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
