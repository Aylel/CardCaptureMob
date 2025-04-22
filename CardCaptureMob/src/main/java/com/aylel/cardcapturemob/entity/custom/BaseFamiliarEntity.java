package com.aylel.cardcapturemob.entity.custom;

import com.aylel.cardcapturemob.entity.CardInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public abstract class BaseFamiliarEntity extends PathfinderMob {

    protected UUID ownerUUID;
    protected CardInstance cardInstance;

    protected BaseFamiliarEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void loadInstanceFromNBT(CardInstance instance) {
        this.cardInstance = instance;
        this.setCustomNameVisible(true);
        this.setCustomName(instance.definition.getDisplayName().copy().append(" (lvl " + instance.level + ")"));
    }

    public CompoundTag saveInstanceToNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.cardInstance != null) {
            tag = this.cardInstance.saveToNBT();
        }
        return tag;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean isAlliedTo(net.minecraft.world.entity.Entity other) {
        if (other instanceof Player player) {
            return player.getUUID().equals(this.ownerUUID);
        }
        return super.isAlliedTo(other);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBox().inflate(5.0D); // pour éviter de le désactiver trop loin
    }

    @Override
    public Component getDisplayName() {
        if (cardInstance != null && cardInstance.definition != null) {
            return cardInstance.definition.getDisplayName().copy().append(" (lvl " + cardInstance.level + ")");
        }
        return super.getDisplayName();
    }
}

