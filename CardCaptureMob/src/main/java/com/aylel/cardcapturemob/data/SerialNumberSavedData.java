package com.aylel.cardcapturemob.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class SerialNumberSavedData extends SavedData {
    public final Map<String, TreeSet<Integer>> usedNumbers = new HashMap<>();

    // CHARGEMENT depuis le disque
    public static SerialNumberSavedData load(CompoundTag tag) {
        SerialNumberSavedData data = new SerialNumberSavedData();
        for (String key : tag.getAllKeys()) {
            ListTag list = tag.getList(key, net.minecraft.nbt.Tag.TAG_INT);
            TreeSet<Integer> set = new TreeSet<>();
            for (int i = 0; i < list.size(); i++) {
                set.add(list.getInt(i));
            }
            data.usedNumbers.put(key, set);
        }
        return data;
    }

    // SAUVEGARDE sur le disque
    @Override
    public CompoundTag save(CompoundTag tag) {
        for (var entry : usedNumbers.entrySet()) {
            ListTag list = new ListTag();
            for (int i : entry.getValue()) {
                list.add(net.minecraft.nbt.IntTag.valueOf(i));
            }
            tag.put(entry.getKey(), list);
        }
        return tag;
    }
}

