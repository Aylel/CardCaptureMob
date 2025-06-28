package com.aylel.cardcapturemob.entity.custom;

import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.CardInstance;
import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.entity.ai.goal.FamiliarAttackRangeGoal;
import com.aylel.cardcapturemob.entity.ai.goal.FollowFamiliarOwnerGoal;
import com.aylel.cardcapturemob.entity.ai.goal.IFamiliarEntity;
import com.aylel.cardcapturemob.data.VanillaStats;
import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.UUID;

public class StrayFamiliarEntity extends Stray implements RangedAttackMob, IFamiliarEntity, CardOwnerEntity {

    private UUID ownerUUID;
    private CardInstance cardInstance;
    private BehaviorMode behaviorMode = BehaviorMode.PASSIVE;
    private UUID familiarUuid;
    private int cardSerial = -1;
    private int killCount = 0; // compteur de kills PERSISTANT

    public static final double AGGRESSIVE_ATTACK_RADIUS = 20.0D;

    public StrayFamiliarEntity(EntityType<? extends Stray> type, Level level) {
        super(type, level);
        // Donne un arc dès la création
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
    }

    // ===== SAUVEGARDE UNIVERSALISÉE DES PV & KILLS (jamais perdu) =====
    @Override
    public void remove(RemovalReason reason) {
        saveCurrentHpAndKillsUniversal();
        super.remove(reason);
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource cause) {
        saveCurrentHpAndKillsUniversal();
        super.die(cause);

        // Gestion KO à la mort + message owner
        if (!this.level().isClientSide) {
            Player owner = this.getOwner();
            if (owner != null) {
                owner.sendSystemMessage(net.minecraft.network.chat.Component.literal("§cVotre vagabond familier a été tué !"));
            }
            UUID familiarUuid = getFamiliarUuid();
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(familiarUuid);
            if (familiarData != null) {
                familiarData.setCurrentHp(0); // KO !
                FamiliarManager.get().updateFamiliarData(familiarData);
            }
        }
    }

    protected void saveCurrentHpAndKillsUniversal() {
        if (!this.level().isClientSide && this.familiarUuid != null) {
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(this.familiarUuid);
            if (familiarData != null) {
                if (this.getHealth() > 0 || familiarData.getCurrentHp() > 0) {
                    familiarData.setCurrentHp(this.getHealth());
                }
                familiarData.setKillCount(this.killCount);
                FamiliarManager.get().updateFamiliarData(familiarData);
            }
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean result = super.doHurtTarget(target);
        if (!this.level().isClientSide && target instanceof net.minecraft.world.entity.LivingEntity && !target.isAlliedTo(this)) {
            if (((net.minecraft.world.entity.LivingEntity)target).isDeadOrDying()) {
                addKill();
            }
        }
        return result;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.level().isClientSide && this.familiarUuid != null) {
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(this.familiarUuid);
            if (familiarData != null) {
                this.killCount = familiarData.getKillCount();
                float hp = familiarData.getCurrentHp();
                if (hp > 0) this.setHealth(hp);
                else this.setHealth(1.0f);
            }
        }
    }

    // ===================== IA ============================
    @Override
    protected void registerGoals() {
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FamiliarAttackRangeGoal(this, AGGRESSIVE_ATTACK_RADIUS, 1.2f, 20));
        this.goalSelector.addGoal(3, new FollowFamiliarOwnerGoal(this, 1.0D, 5.0F, 2.0F));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (cardInstance != null) {
            VanillaStats stats = cardInstance.stats;
            if (stats != null) {
                if (this.getMaxHealth() != stats.hp) {
                    this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.hp);
                    if (this.getHealth() > stats.hp) this.setHealth(stats.hp);
                }
                if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(stats.attack);
                }
                if (this.getAttribute(Attributes.ARMOR) != null) {
                    this.getAttribute(Attributes.ARMOR).setBaseValue(stats.armor);
                }
                if (this.getAttribute(Attributes.ARMOR_TOUGHNESS) != null) {
                    this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(stats.toughness);
                }
                if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(stats.speed);
                }
            }

            int level = cardInstance.level;
            if (level >= 100) this.setScale(1.5f);
            else if (level >= 50) this.setScale(1.2f);
            else this.setScale(0.8f);
        }

        // Forcer l’arc en main tout le temps (même si on le lui enlève)
        if (!this.level().isClientSide) {
            ItemStack mainHand = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (mainHand.isEmpty() || mainHand.getItem() != Items.BOW) {
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
            }
        }

        if (!this.level().isClientSide && this.isAlive()) {
            Player owner = this.getOwner();
            if (owner == null || !owner.isAlive()) {
                this.discard();
            } else {
                int pvActuels = (int) this.getHealth();
                int pvMax = (int) this.getMaxHealth();
                String displayName = "vagabond [" + pvActuels + "/" + pvMax + "♥]";
                this.setCustomName(net.minecraft.network.chat.Component.literal(displayName));
                this.setCustomNameVisible(true);

                if (this.getBehaviorMode() == BehaviorMode.PASSIVE) {
                    if (this.getTarget() != null || this.isAggressive()) {
                        this.setTarget(null);
                        this.setAggressive(false);
                        this.setLastHurtByMob(null);
                        this.setLastHurtMob(null);
                        this.getNavigation().stop();
                    }
                }

                double distanceSq = this.distanceToSqr(owner);
                if (distanceSq > 2500.0D) {
                    this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                }
            }
        }
    }

    private void setScale(float scale) {
        this.setBoundingBox(this.getBoundingBox().move(-this.getX(), -this.getY(), -this.getZ())
                .inflate(scale - 1.0).move(this.getX(), this.getY(), this.getZ()));
        this.refreshDimensions();
    }

    public void loadInstanceFromNBT(CardInstance instance) {
        this.cardInstance = instance;
        if (instance != null && instance.definition != null && instance.definition.passiveBonus != null) {
            double speedBonus = instance.definition.passiveBonus.speed;
            if (speedBonus > 0.0) {
                var attr = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attr != null) attr.setBaseValue(attr.getBaseValue() + speedBonus);
            }
        }
    }

    @Override
    public CompoundTag saveInstanceToNBT() {
        CompoundTag tag = cardInstance != null ? cardInstance.saveToNBT() : new CompoundTag();
        tag.putFloat("CurrentHP", this.getHealth());
        tag.putInt("CardSerial", this.cardSerial);
        tag.putInt("KillCount", this.killCount);
        return tag;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerUUID != null) tag.putUUID("OwnerUUID", ownerUUID);
        if (cardInstance != null) tag.put("CardInstance", cardInstance.saveToNBT());
        tag.putInt("CardSerial", this.cardSerial);
        tag.putInt("KillCount", this.killCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("OwnerUUID")) this.ownerUUID = tag.getUUID("OwnerUUID");
        if (tag.contains("CardInstance")) {
            this.cardInstance = CardInstance.fromNBT(tag.getCompound("CardInstance"));
        }
        if (tag.contains("CardSerial")) {
            this.cardSerial = tag.getInt("CardSerial");
        }
        if (tag.contains("KillCount")) {
            this.killCount = tag.getInt("KillCount");
        }
    }

    private void saveKillsToData() {
        if (!this.level().isClientSide && this.familiarUuid != null) {
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(this.familiarUuid);
            if (familiarData != null) {
                familiarData.setKillCount(this.killCount);
                FamiliarManager.get().updateFamiliarData(familiarData);
            }
        }
    }

    // 📦 Interface CardOwnerEntity
    @Override public UUID getOwnerUUID() { return ownerUUID; }
    @Override public void setOwnerUUID(UUID uuid) { this.ownerUUID = uuid; }
    @Override public CardInstance getCardInstance() { return cardInstance; }
    @Override public void setCardInstance(CardInstance instance) { this.cardInstance = instance; }

    public BehaviorMode getBehaviorMode() { return behaviorMode; }
    public void setBehaviorMode(BehaviorMode mode) {
        this.behaviorMode = mode;
        if (mode == BehaviorMode.PASSIVE) {
            this.setTarget(null);
            this.setAggressive(false);
            this.setLastHurtByMob(null);
            this.setLastHurtMob(null);
            this.getNavigation().stop();
        }
    }

    public Player getOwner() {
        return (this.ownerUUID == null || this.level() == null)
                ? null
                : this.level().getPlayerByUUID(this.ownerUUID);
    }

    @Override
    public boolean isSunBurnTick() {
        // Désactive complètement la brûlure au soleil
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    // Familiar-specific accessors
    public void setFamiliarUuid(UUID uuid) { this.familiarUuid = uuid; }
    public UUID getFamiliarUuid() { return familiarUuid; }
    public int getCardSerial() { return cardSerial; }
    public void setCardSerial(int serial) { this.cardSerial = serial; }
    public int getKillCount() { return killCount; }
    public void setKillCount(int kills) { this.killCount = kills; }
    public void addKill() { this.killCount++; saveKillsToData(); }

    // ==== IFamiliarEntity Implémentations ====
    @Override public LivingEntity asLiving() { return this; }
    @Override public void setTarget(LivingEntity target) { super.setTarget(target); }
    @Override public void setAggressive(boolean aggressive) { super.setAggressive(aggressive); }
    @Override public boolean isAggressive() { return super.isAggressive(); }
    @Override public PathNavigation getNavigation() { return super.getNavigation(); }
    @Override public double distanceToSqr(LivingEntity entity) { return super.distanceToSqr(entity); }
    @Override public Level level() { return super.level(); }
    @Override public LookControl getLookControl() { return super.getLookControl(); }
    @Override public boolean doHurtTarget(LivingEntity target) { return super.doHurtTarget(target); }

    // ==== RangedAttackMob implémentations ====
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0));
        double damage = 2.0;
        if (this.cardInstance != null && this.cardInstance.stats != null) {
            damage = this.cardInstance.stats.attack;
        }
        arrow.setBaseDamage(damage);
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.33333333D) - arrow.getY();
        double dz = target.getZ() - this.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + dist * 0.2, dz, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    public boolean canFireProjectileWeapon(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }
}


