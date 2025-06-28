package com.aylel.cardcapturemob.entity;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.data.CardLoader;
import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.nbt.CompoundTag;

public class CardInstance {
    public CardDefinition definition;  // Le modèle de carte (JSON)
    public int level;
    public int xp;
    public int statPoints; // <-- renommé ici !
    public VanillaStats stats;

    // Création à partir de la définition (pour une nouvelle carte)
    public CardInstance(CardDefinition definition) {
        this.definition = definition;
        this.level = definition.level;
        this.xp = definition.xp;
        this.statPoints = definition.statPoints; // <-- renommé ici !
        this.stats = VanillaStats.randomStats(definition.statPoints);
    }

    // Création à partir du NBT (pour charger)
    public CardInstance(CardDefinition definition, CompoundTag tag) {
        this.definition = definition;
        this.level = tag.getInt("Level");
        this.xp = tag.getInt("XP");
        this.statPoints = tag.getInt("Points"); // <-- renommé ici !
        this.stats = VanillaStats.loadFromNBT(tag.getCompound("Stats")); // <-- ICI
    }

    // Sauvegarde au NBT
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Level", this.level);
        tag.putInt("XP", this.xp);
        tag.putInt("Points", this.statPoints); // <-- renommé ici !
        tag.put("Stats", this.stats.saveToNBT()); // <-- ICI

        if (this.definition != null) {
            tag.putString("DefinitionId", this.definition.id);
        }
        return tag;
    }

    // Recharge un CardInstance à partir du tag
    public static CardInstance fromNBT(CompoundTag tag) {
        if (!tag.contains("DefinitionId")) return null;

        String id = tag.getString("DefinitionId");
        CardDefinition definition = CardLoader.getDefinitionById(id);
        if (definition == null) return null;

        return new CardInstance(definition, tag);
    }

    // Gain d’expérience (inchangé)
    public void gainXp(int amount) {
        this.xp += amount;
        while (this.xp >= xpRequiredForNextLevel()) {
            this.xp -= xpRequiredForNextLevel();
            this.level++;
        }
    }

    public int xpRequiredForNextLevel() {
        double base = definition.xp_base;
        double growth = definition.xp_growth;
        return (int) (base * Math.pow(growth, level - 1));
    }

    // Ajoute un point à une stat vanilla
    public boolean addPointToStat(String statName) {
        if (statPoints <= 0) return false; // <-- renommé ici !
        boolean added = this.stats.addPoint(statName, 1); // à faire dans VanillaStats
        if (added) {
            statPoints--; // <-- renommé ici !
            return true;
        }
        return false;
    }
}





