package com.matyrobbrt.antsportation.compat.patchouli;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.util.Utils;
import com.matyrobbrt.antsportation.util.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliCompat {
    public static final ResourceLocation BOOK_ID = Antsportation.rl(Antsportation.MOD_ID);

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        PatchouliAPI.get().registerFunction("antsportationconfig.server", (path, iStyleStack) -> {
            final var cfg = ServerConfig.BY_PATH.get(path);
            final var val = cfg.get();
            if (val instanceof Integer in) {
                return Utils.getCompressedCount(in);
            } else if (val instanceof StringRepresentable str) {
                return str.getSerializedName();
            }
            return val.toString();
        });
        PatchouliAPI.get().registerCommand("antsportation_boxing_io", (style) -> Utils.getCompressedCount(ServerConfig.CONFIG.boxing().getIORate()));
    }

    private static String getDisplayedText(String in) {
        if (Minecraft.getInstance().getLanguageManager().getSelected().equals("en_ud")) {
            // We need to reverse the string
            return Utils.toUpsideDown(in);
        }
        return in;
    }

    public static ItemStack getBook() {
        return PatchouliAPI.get().getBookStack(BOOK_ID);
    }
}
