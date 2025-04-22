package com.aylel.cardcapturemob.client.renderer;

import com.aylel.cardcapturemob.entity.custom.WolfFamiliarEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WolfFamiliarModel extends GeoModel<WolfFamiliarEntity> {

    @Override
    public ResourceLocation getModelResource(WolfFamiliarEntity animatable) {
        return new ResourceLocation("cardcapturemob", "geo/wolf_familiar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WolfFamiliarEntity animatable) {
        return new ResourceLocation("cardcapturemob", "textures/entity/wolf_familiar.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WolfFamiliarEntity animatable) {
        return new ResourceLocation("cardcapturemob", "animations/wolf_familiar.animation.json");
    }
}


