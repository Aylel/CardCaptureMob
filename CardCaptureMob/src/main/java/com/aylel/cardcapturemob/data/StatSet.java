package com.aylel.cardcapturemob.data;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class StatSet {
    private final Map<String, Integer> stats = new HashMap<>();

    private static final String[] MAJOR_STATS = new String[] {
            "force", "endurance", "intelligence", "volonte",
            "agilite", "vitesse", "chance", "sagesse", "charisme"
    };

    public StatSet() {
        for (String stat : MAJOR_STATS) {
            stats.put(stat, 0);
        }
    }

    public int get(String stat) {
        return stats.getOrDefault(stat.toLowerCase(), 0);
    }

    public void set(String stat, int value) {
        stats.put(stat.toLowerCase(), value);
    }

    public boolean addPoints(String stat, int amount) {
        String key = stat.toLowerCase();
        if (!stats.containsKey(key)) return false;
        stats.put(key, stats.get(key) + amount);
        return true;
    }

    public StatSet copy() {
        StatSet copy = new StatSet();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            copy.set(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            tag.putInt(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        for (String stat : MAJOR_STATS) {
            if (tag.contains(stat)) {
                stats.put(stat, tag.getInt(stat));
            }
        }
    }

    public Map<String, Integer> getAllStats() {
        return stats;
    }
}

