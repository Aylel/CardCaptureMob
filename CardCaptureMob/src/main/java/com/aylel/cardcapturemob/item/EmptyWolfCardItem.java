package com.aylel.cardcapturemob.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import com.aylel.cardcapturemob.registry.ModItems;

public class EmptyWolfCardItem extends EmptyCaptureCardItem {

    public EmptyWolfCardItem(Properties properties) {
        super(properties);
    }

    @Override
    protected EntityType<?> getTargetEntityType() {
        return EntityType.WOLF;
    }

    @Override
    protected double getCaptureChance() {
        return 0.10; // 10% pour rareté basique
    }

    @Override
    protected Item getFilledCardItem() {
        return ModItems.WOLF_CARD.get();
    }
}



