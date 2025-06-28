package com.aylel.cardcapturemob.entity.ai.goal;

import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.custom.BaseFamiliarEntity;
import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.ai.goal.IFamiliarEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class FamiliarAttackGoal extends Goal {

    private final IFamiliarEntity familiar;
    private final double radius;
    private final float speed;
    private LivingEntity target;

    public FamiliarAttackGoal(IFamiliarEntity familiar, double radius, float speed) {
        this.familiar = familiar;
        this.radius = radius;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (familiar.getBehaviorMode() == BehaviorMode.PASSIVE)
            return false;

        target = this.findTarget();

        return target != null && target.isAlive() && familiar.distanceToSqr(target) <= radius * radius;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null
                && target.isAlive()
                && familiar.getBehaviorMode() != BehaviorMode.PASSIVE
                && familiar.distanceToSqr(target) <= radius * radius;
    }

    @Override
    public void start() {
        if (target != null) {
            familiar.setTarget(target);
            familiar.setAggressive(true);
        }
    }

    @Override
    public void stop() {
        familiar.setTarget(null);
        familiar.setAggressive(false);
        target = null;
        familiar.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            familiar.getLookControl().setLookAt(target, 30.0F, 30.0F);
            familiar.getNavigation().moveTo(target, speed);

            double distanceSq = familiar.distanceToSqr(target);
            if (distanceSq < 2.5D) {
                familiar.doHurtTarget(target);
            }
        } else {
            this.stop();
        }
    }

    /**
     * Trouve la cible à attaquer (le mob hostile le plus proche en agressif, ou l’agresseur en défensif)
     */
    private LivingEntity findTarget() {
        if (familiar.getBehaviorMode() == BehaviorMode.AGGRESSIVE) {
            double radius = this.radius; // Utilise le radius de la goal
            AABB range = new AABB(
                    familiar.getX() - radius, familiar.getY() - 4.0, familiar.getZ() - radius,
                    familiar.getX() + radius, familiar.getY() + 4.0, familiar.getZ() + radius
            );
            return familiar.level().getEntitiesOfClass(LivingEntity.class, range)
                    .stream()
                    .filter(entity -> entity instanceof Monster && entity.isAlive())
                    .filter(entity -> entity != familiar.asLiving())
                    .sorted((a, b) -> Double.compare(familiar.distanceToSqr(a), familiar.distanceToSqr(b)))
                    .findFirst()
                    .orElse(null);
        }
        if (familiar.getBehaviorMode() == BehaviorMode.DEFENSIVE) {
            Player owner = familiar.getOwner();
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

