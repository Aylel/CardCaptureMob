package com.aylel.cardcapturemob.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Renderer générique pour familiers avec échelle personnalisée.
 * Permet de rendre tous tes familiers plus grands/petits selon le paramètre scale.
 *
 * @param <T> Type d'entité mob
 * @param <M> Type de modèle associé à ce mob
 */
public class FamiliarSizeRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final float scale;
    private final ResourceLocation textureLocation;

    public FamiliarSizeRenderer(
            EntityRendererProvider.Context context,
            M model,
            float shadowRadius,
            float scale,
            ResourceLocation texture
    ) {
        super(context, model, shadowRadius);
        this.scale = scale;
        this.textureLocation = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return textureLocation;
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(scale, scale, scale);
        // Pas besoin d'appeler super.scale ici, le scale custom suffit !
    }
}

