package com.aylel.cardcapturemob.entity.ai.goal;

import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.custom.WolfFamiliarEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class BehaviorAttackGoal extends Goal {

    private final WolfFamiliarEntity familiar;

    public BehaviorAttackGoal(WolfFamiliarEntity familiar) {
        this.familiar = familiar;
        this.setFlags(EnumSet.of(Flag.TARGET)); // Ce goal concerne la cible à attaquer
    }

    @Override
    public boolean canUse() {
        if (familiar.getBehaviorMode() == BehaviorMode.PASSIVE) {
            return false;
        }

        LivingEntity target = this.findTarget();

        if (target != null) {
            this.familiar.setTarget(target);
            this.familiar.setAggressive(true);
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        // Optionnel : tu peux remettre le loup en non-agressif ici si tu veux
        // familiar.setAggressive(false);
        // familiar.setTarget(null);
    }

    private LivingEntity findTarget() {
        if (familiar.getBehaviorMode() == BehaviorMode.AGGRESSIVE) {
            // Zone de détection
            AABB range = new AABB(
                    familiar.getX() - WolfFamiliarEntity.AGGRESSIVE_ATTACK_RADIUS,
                    familiar.getY() - 4.0,
                    familiar.getZ() - WolfFamiliarEntity.AGGRESSIVE_ATTACK_RADIUS,
                    familiar.getX() + WolfFamiliarEntity.AGGRESSIVE_ATTACK_RADIUS,
                    familiar.getY() + 4.0,
                    familiar.getZ() + WolfFamiliarEntity.AGGRESSIVE_ATTACK_RADIUS
            );

            // --- Prend le monstre le plus proche en priorité ---
            return familiar.level().getEntitiesOfClass(LivingEntity.class, range)
                    .stream()
                    .filter(entity -> entity instanceof Monster && entity.isAlive())
                    .filter(entity -> entity != familiar)
                    .sorted((a, b) -> Double.compare(familiar.distanceToSqr(a), familiar.distanceToSqr(b)))
                    .findFirst()
                    .orElse(null);
        }

        if (familiar.getBehaviorMode() == BehaviorMode.DEFENSIVE) {
            // Défensif : ce que le joueur attaque ou qui attaque le joueur
            var owner = familiar.getOwner();
            if (owner != null && owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive()) {
                return owner.getLastHurtMob();
            }
            if (owner != null && owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive()) {
                return owner.getLastHurtByMob();
            }
        }
        return null;
    }
}


