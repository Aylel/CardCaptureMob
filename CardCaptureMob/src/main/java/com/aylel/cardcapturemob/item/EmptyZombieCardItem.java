package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import net.minecraft.world.item.Item;
import com.aylel.cardcapturemob.entity.custom.ZombieFamiliarEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;

public class EmptyZombieCardItem extends SummonFamiliarCardItem {

    public EmptyZombieCardItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<?> getEntityType() {
        return EntityType.ZOMBIE; // Ici c'est le type Vanilla Zombie, on capture un vrai zombie !
    }

    @Override
    protected CardDefinition getCardDefinition() {
        // Appeler ton CardLoader pour récupérer la définition de la carte vide Zombie
        return com.aylel.cardcapturemob.data.CardLoader.INSTANCE.getDefinition("empty_zombie_card");
    }
}

