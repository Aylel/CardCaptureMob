package com.aylel.cardcapturemob.client;

import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.data.VanillaStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;

public class CardTooltipHelper {

    public static void appendStatsAndLevel(ItemStack stack,
                                           Level world,
                                           List<Component> tooltip,
                                           TooltipFlag flag) {
        CompoundTag root = stack.getTag();
        if (root == null) return;

        // 1. Rareté (titre en gras)
        if (root.contains("rarity")) {
            String raw = root.getString("rarity");
            try {
                CardRarity rarity = CardRarity.valueOf(raw.toUpperCase());
                tooltip.add(
                        Component.empty()
                                .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                                .append(Component.literal(rarity.getDisplayName())
                                        .withStyle(rarity.getChatColor()))
                );
            } catch (IllegalArgumentException e) {
                tooltip.add(
                        Component.empty()
                                .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                                .append(Component.literal(raw))
                );
            }
        }

        // 2. Numéro de la carte (titre en gras)
        if (root.contains("Serial")) {
            tooltip.add(
                    Component.empty()
                            .append(Component.literal("Numéro de la carte : ").withStyle(Style.EMPTY.withBold(true)))
                            .append(Component.literal("#" + root.getInt("Serial")))
            );
        }

        // 3. Capturé par (titre en gras)
        if (root.contains("OwnerName")) {
            tooltip.add(
                    Component.empty()
                            .append(Component.literal("Capturé par : ").withStyle(Style.EMPTY.withBold(true)))
                            .append(Component.literal(root.getString("OwnerName")))
            );
        }

        // 4. Monstres tués et vie actuelle (dynamique, via FamiliarManager/FamiliarData)
        int kills = 0;
        float currentHp = 0;
        float maxHp = 0;
        if (root.hasUUID("FamiliarSerial")) {
            UUID familiarUuid = root.getUUID("FamiliarSerial");
            FamiliarData familiarData = FamiliarManager.get().getFamiliarData(familiarUuid);
            if (familiarData != null) {
                kills = familiarData.getKillCount();
                currentHp = familiarData.getCurrentHp();
                maxHp = familiarData.getHp();
            }
        }
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Monstres tués : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal(String.valueOf(kills)))
        );

        // Affiche la vie actuelle dans tous les cas, pour la lisibilité
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Vie actuelle : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal((int)currentHp + " / " + (int)maxHp))
        );

        // 5. Espace visuel
        tooltip.add(Component.empty());

        // 6. Titre "Statistiques" en gras
        tooltip.add(
                Component.literal("Statistiques").withStyle(Style.EMPTY.withBold(true))
        );

        // 7. Stats de base (PV Max, Attaque, etc.)
        CompoundTag statsTag = root.contains("Stats") ? root.getCompound("Stats") : root;
        VanillaStats stats = VanillaStats.loadFromNBT(statsTag);

        tooltip.add(Component.literal("PV Max      : " + stats.hp));
        tooltip.add(Component.literal("Attaque     : " + stats.attack));
        tooltip.add(Component.literal("Armure      : " + stats.armor));
        tooltip.add(Component.literal("Robustesse  : " + stats.toughness));
        tooltip.add(Component.literal("Vitesse     : " + String.format("%.3f", stats.speed)));

        // Dommage par coup (à la fin des stats pour garder la structure)
        int attackStat = stats.attack;
        int bonus = 0; // À compléter si tu ajoutes des bonus
        int damage = attackStat + bonus;
        tooltip.add(Component.literal("Dommage par coup : " + damage));
    }
}







