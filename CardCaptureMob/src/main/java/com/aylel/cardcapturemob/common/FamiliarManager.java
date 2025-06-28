package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.data.FamiliarSaveData;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamiliarManager {
    private static FamiliarManager INSTANCE;

    private final Map<UUID, FamiliarData> familiars = new HashMap<>();
    private FamiliarSaveData currentSaveData = null;

    public static FamiliarManager get() {
        if (INSTANCE == null) INSTANCE = new FamiliarManager();
        return INSTANCE;
    }

    public void loadFromWorld(ServerLevel serverLevel) {
        this.currentSaveData = FamiliarSaveData.get(serverLevel);
        this.reloadFromSaveData(this.currentSaveData);
    }

    public void setCurrentSaveData(FamiliarSaveData saveData) {
        this.currentSaveData = saveData;
    }

    public void reloadFromSaveData(FamiliarSaveData saveData) {
        this.familiars.clear();
        if (saveData != null) {
            this.familiars.putAll(saveData.getAllFamiliars());
        }
    }

    public void addFamiliar(FamiliarData data) {
        familiars.put(data.getUuid(), data);
        if (currentSaveData != null) {
            currentSaveData.addOrUpdate(data);
        }
    }

    public FamiliarData getFamiliarData(UUID uuid) {
        return familiars.get(uuid);
    }

    public void updateFamiliarData(FamiliarData data) {
        familiars.put(data.getUuid(), data);
        if (currentSaveData != null) {
            currentSaveData.addOrUpdate(data);
        }
    }

    public void saveToWorld(ServerLevel serverLevel) {
        if (currentSaveData != null) {
            serverLevel.getDataStorage().set(FamiliarSaveData.NAME, currentSaveData);
        }
    }

    public void clear() {
        familiars.clear();
        currentSaveData = null;
    }

    public int getFamiliarsCount() {
        return familiars.size();
    }
}

