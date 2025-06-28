package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.data.SerialNumberSavedData;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class SerialNumberManager {
    // ========== Sauvegarde sur le disque ==========
    private static SerialNumberSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                SerialNumberSavedData::load,
                SerialNumberSavedData::new,
                "cardcapture_serials"
        );
    }

    // ==== OBTENIR UN NUMÉRO DISPONIBLE (ILLIMITÉ) ====
    public static int getNextSerial(ServerLevel level, String mobType, CardRarity rarity) {
        SerialNumberSavedData data = getData(level);
        String key = mobType + "|" + rarity.name();
        TreeSet<Integer> used = data.usedNumbers.computeIfAbsent(key, k -> new TreeSet<>());

        // Nouveau numéro = 1 + max utilisé (ou 1 si vide)
        int next = used.isEmpty() ? 1 : used.last() + 1;
        used.add(next);
        data.setDirty();
        return next;
    }

    // ==== LIBÉRER UN NUMÉRO QUAND CARTE DÉTRUITE (optionnel, tu peux le laisser) ====
    public static void releaseSerial(ServerLevel level, String mobType, CardRarity rarity, int serial) {
        SerialNumberSavedData data = getData(level);
        String key = mobType + "|" + rarity.name();
        TreeSet<Integer> used = data.usedNumbers.get(key);
        if (used != null) {
            used.remove(serial);
            data.setDirty();
        }
    }

    // ==== CONTRÔLE DE LA LIMITE (toujours false, illimité) ====
    public static boolean isFull(ServerLevel level, String mobType, CardRarity rarity) {
        // Plus de limite : on retourne toujours false
        return false;
    }
}



