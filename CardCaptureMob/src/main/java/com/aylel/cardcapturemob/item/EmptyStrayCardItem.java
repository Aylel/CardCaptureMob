package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;

public class EmptyStrayCardItem extends SummonFamiliarCardItem {

    public EmptyStrayCardItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<?> getEntityType() {
        return EntityType.STRAY; // Cible vanilla Stray (pour éventuelles interactions)
    }

    @Override
    protected CardDefinition getCardDefinition() {
        return com.aylel.cardcapturemob.data.CardLoader.INSTANCE.getDefinition("empty_stray_card");
    }
}
