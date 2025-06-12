package com.aylel.cardcapturemob.cards;

import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.cards.PassiveBonus;
import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.network.chat.Component;

public class CardDefinition {
    public String id;                 // ex: "empty_wolf_card"
    public String name;               // Nom visible en jeu
    public CardRarity rarity;         // Enum ou String (doit correspondre dans le JSON !)
    public int level;                 // (pas forcément utilisé, mais chargé si présent)
    public int xp;
    public int points_disponibles;
    public int statPoints;            // Points de stats à la capture

    // Table d'expérience (pour plus tard)
    public double xp_base;
    public double xp_growth;

    public VanillaStats stats;              // Stats vanilla pour le monstre
    public PassiveBonus passiveBonus;       // Bonus conféré au joueur/familier

    // --- Champs ajoutés pour correspondre au JSON de settings ---
    public double capture_rate;             // <- IMPORTANT : doit être le même nom/casse que le JSON !
    public double mob_drop_rate;
    public double container_drop_rate;

    // Autres champs éventuels du JSON (min/max count...)
    public Integer min_count;               // nullable si absent du JSON
    public Integer max_count;

    public CardDefinition() {}

    public Component getDisplayName() {
        return Component.literal(name);
    }

    public void setXpTable(double base, double growth) {
        this.xp_base   = base;
        this.xp_growth = growth;
    }
}





