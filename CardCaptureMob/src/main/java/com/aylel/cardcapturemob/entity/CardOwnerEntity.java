package com.aylel.cardcapturemob.entity;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public interface CardOwnerEntity {
    UUID getOwnerUUID();
    void setOwnerUUID(UUID uuid);

    CardInstance getCardInstance();
    void setCardInstance(CardInstance instance);

    CompoundTag saveInstanceToNBT();
    void loadInstanceFromNBT(CardInstance instance);

    BehaviorMode getBehaviorMode();
    void setBehaviorMode(BehaviorMode mode);

    // === Ajout essentiel pour la sauvegarde auto des HP ===
    UUID getFamiliarUuid();
}



