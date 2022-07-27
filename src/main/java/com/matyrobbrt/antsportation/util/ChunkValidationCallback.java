package com.matyrobbrt.antsportation.util;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.registration.AntsportationBlocks;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.world.ForgeChunkManager;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ChunkValidationCallback implements ForgeChunkManager.LoadingValidationCallback {
    private static final Marker MARKER = MarkerFactory.getMarker("ChunkValidation");
    @Override
    public void validateTickets(ServerLevel level, ForgeChunkManager.TicketHelper ticketHelper) {
        ResourceLocation worldName = level.dimension().location();
        log("Validating tickets for: {}. Blocks: {}", worldName, ticketHelper.getBlockTickets().size());
        if (ServerConfig.CONFIG.ants().chunkLoadingMarkers().get()) {
            ticketHelper.getBlockTickets().forEach((pos, tickets) -> {
                final var forcedChunks = tickets.getFirst();
                if (forcedChunks.size() > 0) {
                    final var state = level.getBlockState(pos);
                    if (!state.is(AntsportationBlocks.CHUNK_LOADING_MARKER.get())) {
                        log("Removing tickets for block at position {} as it is no longer a valid chunk loading block.", pos);
                        ticketHelper.removeAllTickets(pos);
                    }
                }
            });
        } else {
            log("Removing all ticks as chunk loading markers are disabled.");
            ticketHelper.getBlockTickets().forEach((pos, $) -> ticketHelper.removeAllTickets(pos));
        }
    }

    private void log(String message, Object... args) {
        Antsportation.LOGGER.debug(MARKER, message, args);
    }
}
