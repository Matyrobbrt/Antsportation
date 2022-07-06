package com.matyrobbrt.antsportation.event;

import com.matyrobbrt.antsportation.Antsportation;
import com.matyrobbrt.antsportation.client.entity.AntQueenModel;
import com.matyrobbrt.antsportation.client.entity.AntQueenRenderer;
import com.matyrobbrt.antsportation.entity.AntQueenEntity;
import com.matyrobbrt.antsportation.registration.AntsportationEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Antsportation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AntsportationEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(AntsportationEntities.ANT_QUEEN.get(), AntQueenEntity.setAttributes());
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(AntQueenModel.LAYER_LOCATION, AntQueenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(AntsportationEntities.ANT_QUEEN.get(), AntQueenRenderer::new);
    }
}
