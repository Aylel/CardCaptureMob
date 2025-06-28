package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.common.CardRarity;
import net.minecraft.nbt.CompoundTag;

public class CardNBTHelper {

    /**
     * Ajoute la rareté (en lowercase) dans le tag
     */
    public static void writeRarity(CompoundTag tag, CardRarity rarity) {
        tag.putString("rarity", rarity.name().toLowerCase());
    }

    /**
     * Lit la rareté depuis le tag, ou retourne BASIC par défaut
     */
    public static CardRarity readRarity(CompoundTag tag) {
        if (tag.contains("rarity")) {
            try {
                return CardRarity.valueOf(tag.getString("rarity").toUpperCase());
            } catch (IllegalArgumentException e) {
                // si la chaîne n'est pas reconnue
            }
        }
        return CardRarity.BASIC;
    }
}
