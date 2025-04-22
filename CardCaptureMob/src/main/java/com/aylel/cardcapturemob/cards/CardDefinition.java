package com.aylel.cardcapturemob.cards;

import com.aylel.cardcapturemob.data.StatSet;
import net.minecraft.network.chat.Component;


public class CardDefinition {
    public String id;                 // ex: "wolf"
    public String name;              // Nom visible dans le jeu
    public String rarity;            // basique, commune, rare, etc.
    public int level;
    public int xp;
    public int points_disponibles;
    public int points_par_niveau;

    // Table d'expérience
    public double xp_base;
    public double xp_growth;

    public StatSet stats;            // Stats du monstre
    public PassiveBonus passiveBonus; // Bonus conféré au joueur et au familier

    public CardDefinition() {}

    public Component getDisplayName() {
        return Component.literal(name);
    }

    public void setXpTable(double base, double growth) {
        this.xp_base = base;
        this.xp_growth = growth;
    }
}


