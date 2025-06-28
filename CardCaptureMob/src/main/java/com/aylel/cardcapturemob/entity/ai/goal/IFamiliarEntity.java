package com.aylel.cardcapturemob.entity.ai.goal;

import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.CardInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.level.Level;

public interface IFamiliarEntity {
    BehaviorMode getBehaviorMode();
    void setBehaviorMode(BehaviorMode mode);
    Player getOwner();
    CardInstance getCardInstance();


    // Accès à l'entité vanilla
    LivingEntity asLiving();

    // Les méthodes vanilla/fonctions IA utilisées par tes goals
    void setTarget(LivingEntity target);
    void setAggressive(boolean aggressive);
    boolean isAggressive();
    PathNavigation getNavigation();
    double distanceToSqr(LivingEntity entity);
    default double getX() { return asLiving().getX(); }
    default double getY() { return asLiving().getY(); }
    default double getZ() { return asLiving().getZ(); }
    Level level();
    LookControl getLookControl();
    boolean doHurtTarget(LivingEntity target);
}


