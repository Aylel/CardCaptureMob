package com.aylel.cardcapturemob.common;

import net.minecraft.ChatFormatting;

public enum CardRarity {
    BASIC     ("Basique",     0.10, 20,  3, 0xFF888888, ChatFormatting.GRAY),
    COMMON    ("Commun",      0.05, 23,  3, 0xFF00AA00, ChatFormatting.GREEN),
    UNCOMMON  ("Uncommun",    0.03, 25,  4, 0xFF008800, ChatFormatting.DARK_GREEN),
    RARE      ("Rare",        0.015,30,  4, 0xFF0000FF, ChatFormatting.BLUE),
    EPIC("Légendaire",  0.005,35,  5, 0xFFAA00FF, ChatFormatting.DARK_PURPLE),
    LEGENDARY("Mythique",    0.001,40,  5, 0xFFFF0000, ChatFormatting.RED);

    private final String         displayName;
    private final double         captureRate;
    private final int            initialStatPoints;
    private final int            basePointsPerLevel;
    private final int            borderColor;       // ARGB complet
    private final ChatFormatting chatColor;

    CardRarity(String displayName,
               double captureRate,
               int initialStatPoints,
               int basePointsPerLevel,
               int borderColor,
               ChatFormatting chatColor) {
        this.displayName        = displayName;
        this.captureRate        = captureRate;
        this.initialStatPoints  = initialStatPoints;
        this.basePointsPerLevel = basePointsPerLevel;
        this.borderColor        = borderColor;
        this.chatColor          = chatColor;
    }

    public String         getDisplayName()       { return displayName; }
    public double         getCaptureRate()       { return captureRate; }
    public int            getInitialStatPoints() { return initialStatPoints; }
    public int            getBasePointsPerLevel(){ return basePointsPerLevel; }
    public ChatFormatting getChatColor()         { return chatColor; }
    public int            getBorderColor()       { return borderColor; }

    public int calculatePointsForLevel(int level) {
        int pts = basePointsPerLevel;
        if (this == COMMON && level % 2 == 0) pts++;
        return pts;
    }
}




