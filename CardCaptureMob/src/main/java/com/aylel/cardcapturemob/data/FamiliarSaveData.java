package com.aylel.cardcapturemob.data;

import com.aylel.cardcapturemob.common.FamiliarData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamiliarSaveData extends SavedData {

    public static final String NAME = "familiar_data";
    public static final String FILE_NAME = "familiars";
    private final Map<UUID, FamiliarData> familiars = new HashMap<>();

    public FamiliarSaveData() {}

    public FamiliarSaveData(Map<UUID, FamiliarData> loaded) {
        this.familiars.putAll(loaded);
    }

    public static FamiliarSaveData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                FamiliarSaveData::load, FamiliarSaveData::new, FILE_NAME
        );
    }

    public Map<UUID, FamiliarData> getAllFamiliars() {
        return familiars;
    }

    public void addOrUpdate(FamiliarData data) {
        familiars.put(data.getUuid(), data);
        setDirty();
        // [DEBUG] supprimé
    }

    // --- SERIALISATION ---
    @Override
    public CompoundTag save(CompoundTag tag) {
        // [DEBUG] supprimé
        ListTag list = new ListTag();
        for (FamiliarData f : familiars.values()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Uuid", f.getUuid());
            entry.putString("MobType", f.getMobType());
            // Enregistre toutes les stats du familier dans un sous-tag (VanillaStats)
            entry.put("Stats", f.getStats().saveToNBT());
            entry.putFloat("CurrentHp", f.getCurrentHp()); // NOUVEAU : vie courante
            list.add(entry);
        }
        tag.put("Familiars", list);
        return tag;
    }

    // --- DESERIALISATION ---
    public static FamiliarSaveData load(CompoundTag tag) {
        Map<UUID, FamiliarData> loaded = new HashMap<>();
        if (tag.contains("Familiars", 9)) {
            ListTag list = tag.getList("Familiars", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                UUID uuid = entry.getUUID("Uuid");
                String mobType = entry.getString("MobType");
                VanillaStats stats = VanillaStats.loadFromNBT(entry.getCompound("Stats"));
                float currentHp = stats.hp; // valeur par défaut
                if (entry.contains("CurrentHp")) {
                    currentHp = entry.getFloat("CurrentHp");
                }
                FamiliarData fd = new FamiliarData(uuid, mobType, stats, currentHp);
                loaded.put(uuid, fd);
            }
        }
        return new FamiliarSaveData(loaded);
    }
}




