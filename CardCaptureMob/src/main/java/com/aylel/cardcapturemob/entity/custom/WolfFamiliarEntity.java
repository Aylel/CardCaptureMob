package com.aylel.cardcapturemob.entity.custom;

import com.aylel.cardcapturemob.entity.CardInstance;
import com.aylel.cardcapturemob.entity.ai.goal.FollowCardOwnerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class WolfFamiliarEntity extends BaseFamiliarEntity implements GeoAnimatable {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected int tickCount;

    public WolfFamiliarEntity(EntityType<? extends BaseFamiliarEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new FollowCardOwnerGoal(this, 1.0D, 5.0F, 2.0F));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.cardInstance != null) {
            int level = this.cardInstance.level;
            if (level >= 100) {
                this.setScale(1.5f);
            } else if (level >= 50) {
                this.setScale(1.2f);
            } else {
                this.setScale(0.8f);
            }
        }
    }

    private void setScale(float scale) {
        this.setBoundingBox(this.getBoundingBox().move(-this.getX(), -this.getY(), -this.getZ())
                .inflate(scale - 1.0).move(this.getX(), this.getY(), this.getZ()));
        this.refreshDimensions();
    }

    @Override
    public void loadInstanceFromNBT(CardInstance instance) {
        super.loadInstanceFromNBT(instance);

        if (instance.definition.passiveBonus != null) {
            double speedBonus = instance.definition.passiveBonus.speed;
            if (speedBonus > 0.0) {
                this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)
                        .setBaseValue(this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).getBaseValue() + speedBonus);
            }
        }
    }

    // === 🧩 Obligatoire pour GeoAnimatable ===

    @Override
    public void registerControllers(ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public double getTick(Object animatable) {
        return this.tickCount;
    }



}




