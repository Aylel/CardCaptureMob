package com.aylel.cardcapturemob;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.Random;

public class CaptureHandler {

    // Tentative de capture d'un mob
    public static boolean attemptCapture(Level level, Player player, Entity target, ItemStack card, double captureChance) {
        // Vérification basique
        if (level.isClientSide || target == null || card == null) return false;

        // Tirage au sort
        Random random = new Random();
        boolean success = random.nextDouble() < captureChance;

        if (success) {
            // On supprimera le mob et on créera la carte pleine plus tard
            target.discard(); // Supprime l'entité du monde (provisoirement)
        }

        return success;
    }
}