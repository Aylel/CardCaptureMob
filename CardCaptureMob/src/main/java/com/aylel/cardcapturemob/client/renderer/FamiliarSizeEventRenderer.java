package com.aylel.cardcapturemob.client.renderer;

import com.aylel.cardcapturemob.entity.custom.ZombieFamiliarEntity;
import com.aylel.cardcapturemob.entity.custom.StrayFamiliarEntity;
import com.aylel.cardcapturemob.registry.ModEntities;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.aylel.cardcapturemob.client.renderer.FamiliarSizeRenderer;

@Mod.EventBusSubscriber(modid = "cardcapturemob", bus = Mod.EventBusSubscriber.Bus.MOD)
public class FamiliarSizeEventRenderer {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // WOLF FAMILIAR
        event.registerEntityRenderer(ModEntities.WOLF_FAMILIAR.get(),
                (EntityRendererProvider.Context context) ->
                        new FamiliarSizeRenderer<>(
                                context,
                                new WolfModel<>(context.bakeLayer(ModelLayers.WOLF)),
                                0.5f,
                                1.5f,
                                new ResourceLocation("minecraft", "textures/entity/wolf/wolf.png")
                        )
        );

        // ZOMBIE FAMILIAR
        event.registerEntityRenderer(ModEntities.ZOMBIE_FAMILIAR.get(),
                (EntityRendererProvider.Context context) ->
                        new MobRenderer<>(context,
                                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f) {
                            @Override
                            public ResourceLocation getTextureLocation(ZombieFamiliarEntity entity) {
                                return new ResourceLocation("minecraft", "textures/entity/zombie/zombie.png");
                            }
                        }
        );

        // STRAY FAMILIAR (renderer vanilla + vêtements bleus manuellement)
        event.registerEntityRenderer(ModEntities.STRAY_FAMILIAR.get(),
                (EntityRendererProvider.Context context) -> {
                    SkeletonRenderer renderer = new SkeletonRenderer(
                            context,
                            ModelLayers.STRAY,
                            ModelLayers.STRAY_INNER_ARMOR,
                            ModelLayers.STRAY_OUTER_ARMOR
                    ) {
                        @Override
                        public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
                            return new ResourceLocation("minecraft", "textures/entity/skeleton/stray.png");
                        }
                    };

                    // ⚠️ AJOUT MANUEL du clothing layer VANILLA (obligatoire pour familiers custom)
                    renderer.addLayer(new StrayClothingLayer<>(renderer, context.getModelSet()));
                    return renderer;
                }
        );
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Rien pour le stray, tout est vanilla déjà
    }
}




























