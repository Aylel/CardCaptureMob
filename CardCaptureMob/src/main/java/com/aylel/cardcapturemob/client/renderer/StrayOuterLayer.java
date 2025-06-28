package com.aylel.cardcapturemob.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class StrayOuterLayer<T extends AbstractSkeleton> extends RenderLayer<T, SkeletonModel<T>> {
    private static final ResourceLocation STRAY_OVERLAY_LOCATION =
            new ResourceLocation("minecraft", "textures/entity/skeleton/stray_overlay.png");
    private final SkeletonModel<T> layerModel;

    public StrayOuterLayer(MobRenderer<T, SkeletonModel<T>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.layerModel = new SkeletonModel<>(modelSet.bakeLayer(ModelLayers.STRAY_OUTER_LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().copyPropertiesTo(this.layerModel);
        this.layerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.layerModel.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(RenderType.entityCutoutNoCull(STRAY_OVERLAY_LOCATION)),
                packedLight,
                net.minecraft.client.renderer.LightTexture.FULL_BRIGHT,
                1f, 1f, 1f, 1f
        );
    }
}
