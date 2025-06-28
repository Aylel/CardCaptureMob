package com.aylel.cardcapturemob.data;

import net.minecraft.nbt.CompoundTag;
import java.util.Random;

public class VanillaStats {
    public int hp;
    public int attack;
    public int armor;
    public int toughness;
    public float speed;

    // Crée des stats aléatoires en fonction d'un nombre de points (statPoints)
    public static VanillaStats randomStats(int statPoints) {
        VanillaStats stats = new VanillaStats();

        // 1 pt minimum par stat (total 4, le reste à répartir)
        stats.hp        = 1;
        stats.attack    = 1;
        stats.armor     = 1;
        stats.toughness = 1;

        int nStats = 4;
        int toDistribute = statPoints;

        Random rand = new Random();
        for (int i = 0; i < toDistribute; i++) {
            switch (rand.nextInt(nStats)) {
                case 0 -> stats.hp++;
                case 1 -> stats.attack++;
                case 2 -> stats.armor++;
                case 3 -> stats.toughness++;
            }
        }

        // Vitesse : de 0.12 à 0.35 (modifiable si besoin)
        float minSpeed = 0.2f;
        float maxSpeed = 0.35f;
        stats.speed = minSpeed + rand.nextFloat() * (maxSpeed - minSpeed);

        return stats;
    }

    // Sauvegarde dans le NBT
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("hp", hp);
        tag.putInt("attack", attack);
        tag.putInt("armor", armor);
        tag.putInt("toughness", toughness);
        tag.putFloat("speed", speed);
        return tag;
    }

    // Charge depuis le NBT
    public static VanillaStats loadFromNBT(CompoundTag tag) {
        VanillaStats stats = new VanillaStats();
        stats.hp        = tag.getInt("hp");
        stats.attack    = tag.getInt("attack");
        stats.armor     = tag.getInt("armor");
        stats.toughness = tag.getInt("toughness");
        stats.speed     = tag.getFloat("speed");
        return stats;
    }

    public boolean addPoint(String statName, int amount) {
        switch (statName.toLowerCase()) {
            case "hp"        -> { this.hp        += amount; return true; }
            case "attack"    -> { this.attack    += amount; return true; }
            case "armor"     -> { this.armor     += amount; return true; }
            case "toughness" -> { this.toughness += amount; return true; }
            // Pour la vitesse, si tu veux la rendre upgradable :
            case "speed"     -> { this.speed     += amount * 0.01f; return true; }
            default          -> { return false; }
        }
    }
}




