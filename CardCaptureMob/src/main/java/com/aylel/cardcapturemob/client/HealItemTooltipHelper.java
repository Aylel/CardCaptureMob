package com.aylel.cardcapturemob.client;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import java.util.List;

public class HealItemTooltipHelper {

    // ------------ PARCHEMINS DE RÉANIMATION ------------

    // BASIQUE : Rareté gris, heal 1 PV
    public static void appendResurrectionBasicTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Basique").withStyle(style -> style.withColor(TextColor.parseColor("gray"))))
        );
        tooltip.add(Component.literal("Parchemin usé, gravé de runes d’autrefois, capable de ramener un familier déchu pour une dernière chance.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Réanime un familier KO avec 1 PV.")));
        tooltip.add(Component.literal("⚠️ Utilisable uniquement sur un familier KO.")
                .withStyle(style -> style.withItalic(true).withColor(TextColor.parseColor("gold"))));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Placez ce parchemin dans votre main secondaire, puis rappelez l’esprit de votre familier mort dans le monde des vivants."))
                .withStyle(style -> style.withItalic(true)));
    }

    // COMMUN : Rareté vert, heal 5 PV
    public static void appendResurrectionAdvancedTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Commun").withStyle(style -> style.withColor(TextColor.parseColor("green"))))
        );
        tooltip.add(Component.literal("Un parchemin éclatant, infusé d’énergies mystiques, capable d’offrir une nouvelle chance à un compagnon tombé.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Réanime un familier KO avec 5 PV.")));
        tooltip.add(Component.literal("⚠️ Utilisable uniquement sur un familier KO.")
                .withStyle(style -> style.withItalic(true).withColor(TextColor.parseColor("gold"))));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Placez ce parchemin dans votre main secondaire, puis rappelez l’esprit de votre familier mort dans le monde des vivants."))
                .withStyle(style -> style.withItalic(true)));
    }

    // SUPRÊME : Rareté bleu, heal 20 PV
    public static void appendResurrectionSupremeTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Rare").withStyle(style -> style.withColor(TextColor.parseColor("blue"))))
        );
        tooltip.add(Component.literal("Parchemin rarissime, enveloppé d’une aura bleutée, renfermant le pouvoir ultime de ramener un familier à la vie avec une grande partie de sa puissance.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Réanime un familier KO avec 20 PV.")));
        tooltip.add(Component.literal("⚠️ Utilisable uniquement sur un familier KO.")
                .withStyle(style -> style.withItalic(true).withColor(TextColor.parseColor("gold"))));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Placez ce parchemin dans votre main secondaire, puis rappelez l’esprit de votre familier mort dans le monde des vivants."))
                .withStyle(style -> style.withItalic(true)));
    }

    // ------------ POTIONS DE SOIN ------------

    // MINEURE : Rareté gris, soin 5 PV
    public static void appendHealMinorTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Basique").withStyle(style -> style.withColor(TextColor.parseColor("gray"))))
        );
        tooltip.add(Component.literal("Une petite fiole de liquide mystérieux, capable de refermer les blessures superficielles de votre familier.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Soigne un familier de 5 PV.")));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Clic-droit sur votre familier invoqué pour le soigner."))
                .withStyle(style -> style.withItalic(true)));
    }

    // MAJEURE : Rareté vert, soin 10 PV
    public static void appendHealMajorTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Commun").withStyle(style -> style.withColor(TextColor.parseColor("green"))))
        );
        tooltip.add(Component.literal("Une potion plus puissante, infusée d’herbes rares et de magie, idéale pour soigner les blessures importantes.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Soigne un familier de 10 PV.")));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Clic-droit sur votre familier invoqué pour le soigner."))
                .withStyle(style -> style.withItalic(true)));
    }

    // SUPRÊME : Rareté bleu, soin 20 PV
    public static void appendHealSupremeTooltip(ItemStack stack, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
                Component.empty()
                        .append(Component.literal("Rareté : ").withStyle(Style.EMPTY.withBold(true)))
                        .append(Component.literal("Rare").withStyle(style -> style.withColor(TextColor.parseColor("blue"))))
        );
        tooltip.add(Component.literal("Une potion rare, à la lueur surnaturelle, rendant une grande partie de sa force à un familier blessée.")
                .withStyle(style -> style.withItalic(true)));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Effet : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Soigne un familier de 20 PV.")));
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("Utilisation : ").withStyle(Style.EMPTY.withBold(true))
                .append(Component.literal("Clic-droit sur votre familier invoqué pour le soigner."))
                .withStyle(style -> style.withItalic(true)));
    }
}



