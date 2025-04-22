package com.aylel.cardcapturemob.client.renderer;

import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = CardCaptureMob.MODID,
        value = Dist.CLIENT,
        bus   = Mod.EventBusSubscriber.Bus.MOD
)
public class ModRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerEntityRenderer(
                ModEntities.WOLF_FAMILIAR.get(),
                WolfFamiliarRenderer::new
        );
    }
}


