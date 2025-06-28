package com.aylel.cardcapturemob.entity.custom;

import com.aylel.cardcapturemob.entity.BehaviorMode;
import com.aylel.cardcapturemob.entity.CardInstance;
import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.entity.ai.goal.FollowCardOwnerGoal;
import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity.RemovalReason;

import java.util.UUID;

import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;

public class WolfFamiliarEntity extends Wolf implements CardOwnerEntity {

    private UUID ownerUUID;
    private CardInstance cardInstance;
    private BehaviorMode behaviorMode = BehaviorMode.PASSIVE;
    private UUID familiarUuid;
    private int cardSerial = -1;

    private int killCount = 0; // Compteur de kills persistant

    public void setFamiliarUuid(UUID uuid) { this.familiarUuid = uuid; }
    public UUID getFamiliarUuid() { return familiarUuid; }
    public int getCardSerial() { return cardSerial; }
    public void setCardSerial(int serial) { this.cardSerial = serial; }
    public int getKillCount() { return killCount; }
    public void setKillCount(int kills) { this.killCount = kills; }
    public void addKill() { this.killCount++; saveKillsToData(); }

    public static final double AGGRESSIVE_ATTACK_RADIUS = 20.0D;

    public WolfFamiliarEntity(EntityType<? extends Wolf> type, Level level) {
        super(type, level);
    }

    // ===== SAUVEGARDE UNIVERSELLE DES PV & KILLS =====
    @Override
    public void remove(RemovalReason reason) {
        saveCurrentHpAndKillsUniversal();
        super.remove(reason);
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource cause) {
        saveCurrentHpAndKillsUniversal();
        super.die(cause);

        // KO lors de la mort (HP = 0)
        if (!this.level().isClientSide) {
            UUID familiarUuid = getFamiliarUuid();
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(familiarUuid);
            if (familiarData != null) {
                familiarData.setCurrentHp(0); // KO
                FamiliarManager.get().updateFamiliarData(familiarData);
            }
        }
    }

    // Nouvelle méthode pour PV actuels ET kills
    protected void saveCurrentHpAndKillsUniversal() {
        if (!this.level().isClientSide && this.familiarUuid != null) {
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(this.familiarUuid);
            if (familiarData != null) {
                // Ne touche pas au KO (HP=0) : on ne remplace pas si déjà KO
                if (this.getHealth() > 0 || familiarData.getCurrentHp() > 0) {
                    familiarData.setCurrentHp(this.getHealth());
                }
                familiarData.setKillCount(this.killCount);
                FamiliarManager.get().updateFamiliarData(familiarData);
            }
        }
    }

    // Ajoute +1 kill à chaque mob tué
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

    // Recharge kills/HP au spawn
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.level().isClientSide && this.familiarUuid != null) {
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(this.familiarUuid);
            if (familiarData != null) {
                this.killCount = familiarData.getKillCount();
                float hp = familiarData.getCurrentHp();
                // On évite de revive un KO si pas résu
                if (hp > 0) this.setHealth(hp);
                else this.setHealth(1.0f);
            }
        }
        // SUPPRIMÉ : toute gestion de taille custom ou setScale
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FollowCardOwnerGoal(this, 1.0D, 5.0F, 2.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new com.aylel.cardcapturemob.entity.ai.goal.BehaviorAttackGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.cardInstance != null) {
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
            // SUPPRIMÉ : toute gestion de taille dynamique ou setScale
        }

        // Suppression si owner mort ou déco
        if (this.ownerUUID != null && this.level() != null && !this.level().isClientSide) {
            Player owner = this.level().getPlayerByUUID(this.ownerUUID);
            if (owner == null || !owner.isAlive()) {
                this.discard();
            } else {
                // Stop agression si mode PASSIVE
                if (this.getBehaviorMode() == BehaviorMode.PASSIVE) {
                    if (this.getTarget() != null || this.isAggressive()) {
                        this.setTarget(null);
                        this.setAggressive(false);
                        this.setLastHurtByMob(null);
                        this.setLastHurtMob(null);
                        this.getNavigation().stop();
                    }
                }
                // TP auto si trop éloigné
                double distanceSq = this.distanceToSqr(owner);
                if (distanceSq > 2500.0D) {
                    this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                }
            }
        }

        // MAJ dynamique du nom avec HP + kills
        if (!this.level().isClientSide && this.isAlive()) {
            int pvActuels = (int) this.getHealth();
            int pvMax = (int) this.getMaxHealth();
            String displayName = "Wolf [" + pvActuels + "/" + pvMax + "♥]";
            this.setCustomName(net.minecraft.network.chat.Component.literal(displayName));
            this.setCustomNameVisible(true);
        }
    }

    // SUPPRIMÉ : méthode setScale(float scale)

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

    public CompoundTag saveInstanceToNBT() {
        CompoundTag tag = cardInstance != null ? cardInstance.saveToNBT() : new CompoundTag();
        tag.putFloat("CurrentHP", this.getHealth());
        tag.putInt("CardSerial", this.cardSerial);
        tag.putInt("KillCount", this.killCount); // SAUVEGARDE KILLS
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

    // Sauvegarde aussi les kills dans FamiliarData (vente etc)
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
    public BehaviorMode getBehaviorMode() { return this.behaviorMode; }
    public void setBehaviorMode(BehaviorMode mode) {
        this.behaviorMode = mode;
        if (!this.level().isClientSide && (mode == BehaviorMode.AGGRESSIVE || mode == BehaviorMode.DEFENSIVE)) {
            boolean alreadySet = this.targetSelector.getAvailableGoals().stream()
                    .anyMatch(goal -> goal.getGoal() instanceof com.aylel.cardcapturemob.entity.ai.goal.BehaviorAttackGoal);
            if (!alreadySet) {
                this.targetSelector.addGoal(1, new com.aylel.cardcapturemob.entity.ai.goal.BehaviorAttackGoal(this));
            }
        }
    }

    public Player getOwner() {
        if (this.ownerUUID == null) return null;
        return this.level().getPlayerByUUID(this.ownerUUID);
    }

    @Override
    public net.minecraft.world.InteractionResult interactAt(
            net.minecraft.world.entity.player.Player player,
            net.minecraft.world.phys.Vec3 vec,
            net.minecraft.world.InteractionHand hand
    ) {
        return super.interactAt(player, vec, hand);
    }
}

























