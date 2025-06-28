package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;

public class FamiliarData {
    private final UUID uuid;
    private final String mobType;
    private VanillaStats stats;
    private float currentHp; // vie courante
    private int killCount = 0; // compteur de kills

    public FamiliarData(UUID uuid, String mobType, VanillaStats stats) {
        this.uuid = uuid;
        this.mobType = mobType;
        this.stats = stats;
        this.currentHp = stats.hp; // à la création : full life
        this.killCount = 0;
    }

    public FamiliarData(UUID uuid, String mobType, VanillaStats stats, float currentHp) {
        this.uuid = uuid;
        this.mobType = mobType;
        this.stats = stats;
        this.currentHp = currentHp;
        this.killCount = 0;
    }

    public FamiliarData(UUID uuid, String mobType, VanillaStats stats, float currentHp, int killCount) {
        this.uuid = uuid;
        this.mobType = mobType;
        this.stats = stats;
        this.currentHp = currentHp;
        this.killCount = killCount;
    }

    public UUID getUuid() { return uuid; }
    public String getMobType() { return mobType; }
    public VanillaStats getStats() { return stats; }
    public void setStats(VanillaStats stats) { this.stats = stats; }

    public float getCurrentHp() { return currentHp; }
    public void setCurrentHp(float hp) {
        float max = getHp();
        if (hp > max) hp = max;
        if (hp < 0) hp = 0;
        this.currentHp = hp;
    }

    // HP Max (modifiable via stats)
    public int getHp() { return stats.hp; }
    public int getArmor() { return stats.armor; }
    public int getToughness() { return stats.toughness; }
    public int getAttack() { return stats.attack; }
    public float getSpeed() { return stats.speed; }

    // Kills
    public int getKillCount() { return killCount; }
    public void setKillCount(int kills) { this.killCount = kills; }
    public void addKill() { this.killCount++; }

    // --- SAUVEGARDE NBT ---
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("UUID", uuid);
        tag.putString("MobType", mobType);
        tag.put("Stats", stats.saveToNBT());
        tag.putFloat("CurrentHp", currentHp);
        tag.putInt("KillCount", killCount);
        return tag;
    }

    // --- CHARGEMENT NBT ---
    public static FamiliarData loadFromNBT(CompoundTag tag) {
        UUID uuid = tag.getUUID("UUID");
        String mobType = tag.getString("MobType");
        VanillaStats stats = VanillaStats.loadFromNBT(tag.getCompound("Stats"));
        float currentHp = stats.hp;
        if (tag.contains("CurrentHp")) currentHp = tag.getFloat("CurrentHp");
        int killCount = 0;
        if (tag.contains("KillCount")) killCount = tag.getInt("KillCount");
        return new FamiliarData(uuid, mobType, stats, currentHp, killCount);
    }
}






