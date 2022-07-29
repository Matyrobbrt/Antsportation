package com.matyrobbrt.antsportation.block;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.block.entity.MarkerBE;
import com.matyrobbrt.antsportation.data.DatagenHelper;
import com.matyrobbrt.antsportation.registration.AntsportationItems;
import com.matyrobbrt.antsportation.util.Translations;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.world.ForgeChunkManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class ChunkLoadingMarkerBlock extends MarkerBlock {
    public ChunkLoadingMarkerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide && ServerConfig.CONFIG.ants().chunkLoadingMarkers().get() && pLevel instanceof ServerLevel server) {
            final var chunk = pLevel.getChunkAt(pPos).getPos();
            ForgeChunkManager.forceChunk(server, Antsportation.MOD_ID, pPos, chunk.x, chunk.z, true, true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        if (!pLevel.isClientSide && pLevel instanceof ServerLevel server) {
            final var chunk = pLevel.getChunkAt(pPos).getPos();
            ForgeChunkManager.forceChunk(server, Antsportation.MOD_ID, pPos, chunk.x, chunk.z, false, true);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRandom) {
        for (int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
            pLevel.addParticle(ParticleTypes.END_ROD, pPos.getX() + 0.5, pPos.getY(), pPos.getZ()  + 0.5,
                    0, 0, 0);
        }
    }

    @Override
    public void generateRecipes(DatagenHelper helper) {
        helper.shapeless(this)
                .requires(Items.ENDER_EYE, 3)
                .requires(AntsportationItems.MARKER.get(), 1);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new MarkerBE(pPos, pState).withDefaultColor(DyeColor.LIME);
    }

    @Override
    public @NotNull List<Component> getInfo() {
        return List.of(Translations.JEI_CHUNK_LOADING_MARKER.translate());
    }
}
