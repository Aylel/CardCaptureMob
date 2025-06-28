package com.aylel.cardcapturemob.entity.ai.goal;

import com.aylel.cardcapturemob.entity.ai.goal.IFamiliarEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FollowFamiliarOwnerGoal extends Goal {
    private final IFamiliarEntity familiar;
    private final double speed;
    private final float startDistance;
    private final float stopDistance;

    public FollowFamiliarOwnerGoal(IFamiliarEntity familiar, double speed, float startDistance, float stopDistance) {
        this.familiar = familiar;
        this.speed = speed;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        Player owner = familiar.getOwner();
        // On utilise asLiving() pour distanceTo, car l'interface IFamiliarEntity ne définit pas cette méthode
        return owner != null && familiar.asLiving().distanceTo(owner) > startDistance;
    }

    @Override
    public void tick() {
        Player owner = familiar.getOwner();
        if (owner != null && familiar.asLiving().distanceTo(owner) > stopDistance) {
            Vec3 target = owner.position();
            familiar.getNavigation().moveTo(target.x, target.y, target.z, speed);
        }
    }
}

