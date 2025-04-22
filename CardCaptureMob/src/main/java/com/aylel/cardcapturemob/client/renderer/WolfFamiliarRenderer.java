package com.aylel.cardcapturemob.client.renderer;

import com.aylel.cardcapturemob.client.renderer.WolfFamiliarModel;      // <-- même package que ton renderer
import com.aylel.cardcapturemob.entity.custom.WolfFamiliarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;    // <-- à prendre dans minecraft.client.renderer.entity
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WolfFamiliarRenderer extends GeoEntityRenderer<WolfFamiliarEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("cardcapturemob", "textures/entity/wolf/wolf.png");

    public WolfFamiliarRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WolfFamiliarModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull WolfFamiliarEntity entity) {
        return TEXTURE;
    }
}








