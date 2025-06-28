package com.aylel.cardcapturemob.util;

import com.aylel.cardcapturemob.registry.ModItems;
import com.aylel.cardcapturemob.common.FamiliarData;
import com.aylel.cardcapturemob.common.FamiliarManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ResurrectScrollEffect {

    /**
     * Applique l'effet du parchemin de résurrection à la carte de familier KO.
     * Retourne true si la carte a été soignée, false sinon.
     */
    public static boolean apply(ItemStack card, ItemStack scroll) {
        if (card == null || scroll == null) return false;
        if (!card.hasTag()) return false;
        if (!card.getTag().contains("Stats")) return false;

        CompoundTag tag = card.getTag();

        UUID familiarUuid = null;
        if (tag.hasUUID("FamiliarSerial")) {
            familiarUuid = tag.getUUID("FamiliarSerial");
        }
        if (familiarUuid == null) return false;

        FamiliarData familiarData = FamiliarManager.get().getFamiliarData(familiarUuid);
        if (familiarData == null) return false;

        float maxHp = familiarData.getHp();
        float currentHp = familiarData.getCurrentHp();
        if (currentHp > 0f) return false; // Pas KO

        float healPercent = 0f;

        // -- DÉTERMINE LE TYPE DE PARCHEMIN --
        if (scroll.is(ModItems.RESURRECTION_SCROLL_BASIC.get())) {
            healPercent = 0.1f;
        } else if (scroll.is(ModItems.RESURRECTION_SCROLL_ADVANCED.get())) {
            healPercent = 0.5f;
        } else if (scroll.is(ModItems.RESURRECTION_SCROLL_SUPREME.get())) {
            healPercent = 1.0f;
        }

        // Correction : arrondi supérieur et minimum 1
        float healAmount = (float) Math.ceil(maxHp * healPercent);
        if (healAmount < 1f) healAmount = 1f;

        if (healAmount > 0f && scroll.getCount() > 0) {
            familiarData.setCurrentHp(healAmount);
            FamiliarManager.get().updateFamiliarData(familiarData);

            tag.putFloat("CurrentHp", healAmount);
            tag.putBoolean("JustResurrected", true);

            scroll.shrink(1); // Consomme le parchemin ici uniquement !
            System.out.println("[ResurrectScrollEffect] Familier réanimé pour " + healAmount + " PV !");
            return true;
        }
        return false;
    }
}


