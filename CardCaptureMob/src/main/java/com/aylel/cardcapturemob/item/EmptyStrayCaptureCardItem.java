package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.data.CardLootLoader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class EmptyStrayCaptureCardItem extends EmptyCaptureCardItem {

    public EmptyStrayCaptureCardItem(Item.Properties properties) {
        super(properties);
    }

    private CardDefinition getDefinition() {
        return CardLoader.getDefinitionById("empty_stray_card");
    }

    @Override
    protected EntityType<?> getTargetEntityType() {
        return EntityType.STRAY;
    }

    @Override
    protected double getCaptureChance() {
        CardLootLoader.LootConfig cfg = CardLootLoader.get("empty_stray_card");
        if (cfg != null && cfg.capture_rate > 0) {
            return cfg.capture_rate;
        }
        return 0.05D;
    }

    @Override
    protected Item getFilledCardItem() {
        return com.aylel.cardcapturemob.registry.ModItems.STRAY_CARD.get();
    }

    @Override
    protected CardRarity getRarity() {
        CardDefinition def = getDefinition();
        if (def != null && def.rarity != null) {
            return def.rarity;
        }
        return CardRarity.RARE; // ou la rareté que tu veux pour le stray
    }
}
