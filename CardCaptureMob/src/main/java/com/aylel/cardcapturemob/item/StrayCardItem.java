package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.entity.custom.StrayFamiliarEntity;
import com.aylel.cardcapturemob.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class StrayCardItem extends SummonStrayCardItem<StrayFamiliarEntity> {

    public StrayCardItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<? extends StrayFamiliarEntity> getEntityType() {
        return ModEntities.STRAY_FAMILIAR.get();
    }

    @Override
    protected CardDefinition getCardDefinition() {
        return CardLoader.INSTANCE.getDefinition("stray_card");
    }
}

