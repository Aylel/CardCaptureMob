package com.aylel.cardcapturemob.entity;

public enum BehaviorMode {
    PASSIVE,
    DEFENSIVE,
    AGGRESSIVE;

    // 🔥 Petit bonus pratique pour cycler automatiquement
    public BehaviorMode next() {
        return switch (this) {
            case PASSIVE -> DEFENSIVE;
            case DEFENSIVE -> AGGRESSIVE;
            case AGGRESSIVE -> PASSIVE;
        };
    }

    // 🔥 Bonus : joli nom à afficher
    public String getDisplayName() {
        return switch (this) {
            case PASSIVE -> "Passif";
            case DEFENSIVE -> "Défensif";
            case AGGRESSIVE -> "Agressif";
        };
    }
}
