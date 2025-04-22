package com.aylel.cardcapturemob.entity.ai.goal;

import com.aylel.cardcapturemob.entity.custom.BaseFamiliarEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FollowCardOwnerGoal extends Goal {

    private final BaseFamiliarEntity familiar;
    private final double speed;
    private final float stopDistance;
    private final float startDistance;
    private Player owner;

    public FollowCardOwnerGoal(BaseFamiliarEntity familiar, double speed, float startDistance, float stopDistance) {
        this.familiar = familiar;
        this.speed = speed;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (familiar.getOwnerUUID() == null) return false;
        owner = familiar.level().getPlayerByUUID(familiar.getOwnerUUID());
        if (owner == null || owner.isSpectator()) return false;

        return familiar.distanceToSqr(owner) > (startDistance * startDistance);
    }

    @Override
    public boolean canContinueToUse() {
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
