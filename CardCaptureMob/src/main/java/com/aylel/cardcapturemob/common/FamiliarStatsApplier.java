package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FamiliarStatsApplier {
    public static void apply(LivingEntity entity, VanillaStats stats, float currentHp) {
        if (entity.getAttribute(Attributes.MAX_HEALTH) != null)
            entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.hp);

        float safeHp = Math.max(1, Math.min(currentHp, stats.hp));
        entity.setHealth(safeHp);

        if (entity.getAttribute(Attributes.ATTACK_DAMAGE) != null)
            entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(stats.attack);

        if (entity.getAttribute(Attributes.ARMOR) != null)
            entity.getAttribute(Attributes.ARMOR).setBaseValue(stats.armor);

        if (entity.getAttribute(Attributes.ARMOR_TOUGHNESS) != null)
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(stats.toughness);

        if (entity.getAttribute(Attributes.MOVEMENT_SPEED) != null)
            entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(stats.speed);
    }
}
