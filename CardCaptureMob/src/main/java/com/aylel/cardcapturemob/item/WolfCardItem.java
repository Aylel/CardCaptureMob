package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.entity.custom.BaseFamiliarEntity;
import com.aylel.cardcapturemob.registry.ModEntities;
import net.minecraft.world.entity.EntityType;

public class WolfCardItem extends SummonFamiliarCardItem {

    public WolfCardItem(Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<? extends BaseFamiliarEntity> getEntityType() {
        return ModEntities.WOLF_FAMILIAR.get();
    }

    @Override
    protected CardDefinition getCardDefinition() {
        return CardLoader.INSTANCE.getDefinition("wolf");
    }
}


