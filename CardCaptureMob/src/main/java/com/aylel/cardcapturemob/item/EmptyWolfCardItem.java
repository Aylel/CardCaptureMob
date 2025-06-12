package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.data.CardLootLoader;
import com.aylel.cardcapturemob.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class EmptyWolfCardItem extends EmptyCaptureCardItem {

    public EmptyWolfCardItem(Item.Properties properties) {
        super(properties);
    }

    // Utilise l’id pour charger la définition
    private CardDefinition getDefinition() {
        // L'id dans ton fichier JSON est "empty_wolf_card"
        return CardLoader.getDefinitionById("empty_wolf_card");
    }

    @Override
    protected EntityType<?> getTargetEntityType() {
        return EntityType.WOLF;
    }

    @Override
    protected double getCaptureChance() {
        CardLootLoader.LootConfig cfg = CardLootLoader.get("empty_wolf_card");
        if (cfg != null && cfg.capture_rate > 0) {
            return cfg.capture_rate;
        }
        return 0.10D;
    }

    @Override
    protected Item getFilledCardItem() {
        return ModItems.WOLF_CARD.get();
    }

    @Override
    protected CardRarity getRarity() {
        CardDefinition def = getDefinition();
        if (def != null && def.rarity != null) {
            return def.rarity;
        }
        return CardRarity.BASIC;
    }
}






