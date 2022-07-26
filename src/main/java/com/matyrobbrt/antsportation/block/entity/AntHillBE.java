package com.matyrobbrt.antsportation.block.entity;

import com.matyrobbrt.antsportation.compat.top.TOPContext;
import com.matyrobbrt.antsportation.compat.top.TOPInfoDriver;
import com.matyrobbrt.antsportation.entity.AntSoldierEntity;
import com.matyrobbrt.antsportation.entity.AntWorkerEntity;
import com.matyrobbrt.antsportation.entity.HillAntSoldierEntity;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import com.matyrobbrt.antsportation.util.AntTarget;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class AntHillBE extends BlockEntity implements TOPInfoDriver {
    private static final IntSupplier SPAWN_RATE = ServerConfig.CONFIG.ants().hillSummonRate()::get;
    private static final int SUMMON_SOLDIER_INTERVAL = 20 * 60;
    private static final int PREFERRED_SOLDIER_AMOUNT = 3;

    public final AntHillBE.Inventory inventory = new Inventory();
    public boolean hasQueen = false;
    public BlockPos nextMarker;

    private int progressTicks;

    public AntHillBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AntsportationBlocks.ANT_HILL_BE.get(), pWorldPosition, pBlockState);
    }

    public void tick() {
        passivelySoliderSpawn();

        if (hasQueen) {
            progressTicks++;
            if (progressTicks >= SPAWN_RATE.getAsInt()) {
                progressTicks = 0;
                if (nextMarker == null || !isMarkerValid(nextMarker)) {
                    recalculateNextMarker();
                }
                if (nextMarker != null) {
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        final var stack = inventory.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            final ItemStack itemStack = stack.copy();
                            final var amount = Math.min(stack.getMaxStackSize(), stack.getCount());
                            itemStack.setCount(amount);
                            summonAnt(itemStack);
                            inventory.extractItem(i, amount, false);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isMarkerValid(BlockPos pos) {
        final var state = getLevel().getBlockState(pos);
        return state.getBlock() instanceof AntTarget target && target.isValidTarget(state, pos, getLevel());
    }

    private void recalculateNextMarker() {
        nextMarker = findNearestBlock(this.getBlockPos(), this::isMarkerValid, 10)
                .orElse(null);
    }

    @SuppressWarnings("unused")
    private void passivelySoliderSpawn() {
        if (getLevel().getGameTime() % SUMMON_SOLDIER_INTERVAL == 0) {
            final var soldiersNearby = getLevel().getEntitiesOfClass(AntSoldierEntity.class, new AABB(worldPosition).inflate(10));
            final var toSpawn = PREFERRED_SOLDIER_AMOUNT - soldiersNearby.size();
            for (int i = 0; i < toSpawn; i++) {
                final var solider = new HillAntSoldierEntity(AntsportationEntities.HILL_ANT_SOLDIER.get(), getLevel());
                final var blockpos = worldPosition.offset(-2 + solider.getRandom().nextInt(5), 1, -2 + solider.getRandom().nextInt(5));
                if (!getLevel().getBlockState(blockpos).isAir()) continue;
                solider.setTargetPos(blockpos);
                solider.moveTo(blockpos, 0.0F, 0.0F);
                getLevel().addFreshEntity(solider);
            }
        }
    }

    private void summonAnt(ItemStack stack) {
        final var antWorker = new AntWorkerEntity(AntsportationEntities.ANT_WORKER.get(), level);
        antWorker.setItemSlot(EquipmentSlot.OFFHAND, stack);
        antWorker.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5);
        antWorker.setNextMarker(nextMarker);
        antWorker.nodeHistory.add(this.getBlockPos());
        getLevel().addFreshEntity(antWorker);
        setChanged();
    }

    @NotNull // Why would it ever be null?
    @Override
    public Level getLevel() {
        // noinspection ConstantConditions
        return super.getLevel();
    }

    @SuppressWarnings("DuplicatedCode")
    public Optional<BlockPos> findNearestBlock(BlockPos searchPos, Predicate<BlockPos> predicate, double pDistance) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; (double) i <= pDistance; i = i > 0 ? -i : 1 - i) {
            for (int j = 0; (double) j < pDistance; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        pos.setWithOffset(searchPos, k, i - 1, l);
                        if (searchPos.closerThan(pos, pDistance) && !searchPos.equals(pos) && predicate.test(pos)) {
                            return Optional.of(pos.immutable());
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", inventory.serializeNBT());
        pTag.putBoolean("hasQueen", hasQueen);
        if (nextMarker != null) {
            pTag.put("nextMarker", NbtUtils.writeBlockPos(nextMarker));
        }
        pTag.putInt("progressTicks", progressTicks);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        hasQueen = nbt.getBoolean("hasQueen");
        nbt.getCompound("nextMarker");
        nextMarker = NbtUtils.readBlockPos(nbt.getCompound("nextMarker"));
        progressTicks = nbt.getInt("progressTicks");
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

    @Override
    public void addInfo(TOPContext context) {
        if (hasQueen) {
            context.text(Translations.TOP_TICKS_UNTIL_SPAWN.translate(SPAWN_RATE.getAsInt() - progressTicks));
        }
    }

    class Inventory extends ItemStackHandler {
        public Inventory() {
            super(10);
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

    public ItemStack addItem(ItemStack item){
        return ItemHandlerHelper.insertItem(inventory, item, false);
    }
}
