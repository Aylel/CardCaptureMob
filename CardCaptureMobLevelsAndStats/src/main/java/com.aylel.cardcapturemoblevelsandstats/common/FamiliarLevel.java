package com.aylel.cardcapturemoblevelsandstats.common;

public class FamiliarLevel {
    private int level;
    private int currentXp;
    private int pointsAvailable;  // <- on l’ajoute ici

    /** Niveau 1, XP 0, 0 points dispo */
    public FamiliarLevel() {
        this.level = 1;
        this.currentXp = 0;
        this.pointsAvailable = 0;
    }

    /** Constructeur complet si tu veux restaurer un état existant */
    public FamiliarLevel(int level, int currentXp, int pointsAvailable) {
        this.level = level;
        this.currentXp = currentXp;
        this.pointsAvailable = pointsAvailable;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    /** Renvoie le nombre de points de stat disponibles */
    public int getPointsAvailable() {
        return pointsAvailable;
    }

    /**
     * Ajoute de l'XP et gère la montée de niveau.
     * Pour l’instant, on ne calcule pas encore les points bonus ici.
     */
    public void addXp(int amount) {
        this.currentXp += amount;
        while (currentXp >= xpForNextLevel()) {
            currentXp -= xpForNextLevel();
            level++;
            // Ici, tu pourras plus tard faire :
            // int bonus = taRareté.calculatePointsForLevel(level);
            // this.pointsAvailable += bonus;
        }
    }

    /**
     * XP nécessaire pour passer au niveau suivant.
     * (Formule = level * 100, modifiable.)
     */
    public int xpForNextLevel() {
        return level * 100;
    }
}

