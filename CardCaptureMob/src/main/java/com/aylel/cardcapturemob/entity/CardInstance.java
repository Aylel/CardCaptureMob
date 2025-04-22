package com.aylel.cardcapturemob.entity;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.data.StatSet;
import net.minecraft.nbt.CompoundTag;

public class CardInstance {
    public CardDefinition definition;  // Le modèle de carte (JSON)
    public int level;
    public int xp;
    public int pointsDisponibles;
    public StatSet stats;

    public CardInstance(CardDefinition definition) {
        this.definition = definition;
        this.level = definition.level;
        this.xp = definition.xp;
        this.pointsDisponibles = definition.points_disponibles;
        this.stats = definition.stats.copy(); // On part d’une base
    }

    public CardInstance(CardDefinition definition, CompoundTag tag) {
        this.definition = definition;
        this.level = tag.getInt("Level");
        this.xp = tag.getInt("XP");
        this.pointsDisponibles = tag.getInt("Points");
        this.stats = new StatSet();
        this.stats.loadFromNBT(tag.getCompound("Stats"));
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Level", this.level);
        tag.putInt("XP", this.xp);
        tag.putInt("Points", this.pointsDisponibles);
        tag.put("Stats", this.stats.saveToNBT());
        return tag;
    }

    // Gain d’expérience
    public void gainXp(int amount) {
        this.xp += amount;
        while (this.xp >= xpRequiredForNextLevel()) {
            this.xp -= xpRequiredForNextLevel();
            this.level++;
            this.pointsDisponibles += definition.points_par_niveau;
        }
    }

    // XP demandée pour le prochain niveau
    public int xpRequiredForNextLevel() {
        double base = definition.xp_base;
        double growth = definition.xp_growth;
        return (int) (base * Math.pow(growth, level - 1));
    }

    // Ajouter un point à une stat si possible
    public boolean addPointToStat(String statName) {
        if (pointsDisponibles <= 0) return false;
        if (stats.addPoints(statName, 1)) {
            pointsDisponibles--;
            return true;
        }
        return false;
    }
}

