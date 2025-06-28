package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.entity.custom.ZombieFamiliarEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import com.aylel.cardcapturemob.registry.ModEntities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ZombieCardItem extends SummonZombieCardItem<ZombieFamiliarEntity> {

    public ZombieCardItem(Item.Properties properties) {super(properties);
    }

    @Override
    protected EntityType<? extends ZombieFamiliarEntity> getEntityType() {
        return ModEntities.ZOMBIE_FAMILIAR.get();
    }

    @Override
    protected CardDefinition getCardDefinition() {
        return CardLoader.INSTANCE.getDefinition("zombie_card");
    }

}



