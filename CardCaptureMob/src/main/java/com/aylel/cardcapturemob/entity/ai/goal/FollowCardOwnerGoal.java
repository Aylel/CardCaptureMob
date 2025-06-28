package com.aylel.cardcapturemob.entity.ai.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;


public class FollowCardOwnerGoal extends Goal {

    private final TamableAnimal familiar;
    private final double speed;
    private final float stopDistance;
    private final float startDistance;
    private Player owner;

    public FollowCardOwnerGoal(TamableAnimal familiar, double speed, float startDistance, float stopDistance) {
        this.familiar = familiar;
        this.speed = speed;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (familiar.getTarget() != null) return false;

        if (familiar.getOwnerUUID() == null) return false;
        owner = familiar.level().getPlayerByUUID(familiar.getOwnerUUID());
        return owner != null && !owner.isSpectator() && familiar.distanceToSqr(owner) > (startDistance * startDistance);
    }


    @Override
    public boolean canContinueToUse() {
        if (familiar.getTarget() != null) return false; // 🔥 Ne pas continuer à suivre si on est en combat
        return owner != null && familiar.distanceToSqr(owner) > (stopDistance * stopDistance);
    }


    @Override
    public void start() {
        if (owner != null) {
            familiar.getNavigation().moveTo(owner, speed);
        }
    }

    @Override
    public void stop() {
        familiar.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (owner != null) {
            familiar.getLookControl().setLookAt(owner, 10.0F, familiar.getMaxHeadXRot());
            if (familiar.distanceToSqr(owner) > 2.5D) {
                familiar.getNavigation().moveTo(owner, speed);
            }
        }
    }
}

