package com.aylel.cardcapturemob.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;

import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.client.HealItemTooltipHelper;

public class FamiliarHealPotionItem extends Item {
    private final float healAmount;

    public FamiliarHealPotionItem(Properties properties, float healAmount) {
        super(properties.stacksTo(64));
        this.healAmount = healAmount;
    }

    public float getHealAmount() {
        return this.healAmount;
    }

    // Tooltip dynamique selon la valeur de heal
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flag) {
        if (healAmount == 5f) {
            HealItemTooltipHelper.appendHealMinorTooltip(stack, tooltip, flag);
        } else if (healAmount == 10f) {
            HealItemTooltipHelper.appendHealMajorTooltip(stack, tooltip, flag);
        } else if (healAmount == 20f) {
            HealItemTooltipHelper.appendHealSupremeTooltip(stack, tooltip, flag);
        } else {
            // Fallback générique si jamais tu ajoutes d'autres valeurs plus tard
            tooltip.add(net.minecraft.network.chat.Component.literal("Potion de soin mystérieuse..."));
        }
    }

    // Utilisé par l'event dans ModEvents pour soigner tous types de familiers
    public static boolean tryHealFamiliar(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof CardOwnerEntity) {
            float healAmount = 0f;
            if (stack.getItem() instanceof FamiliarHealPotionItem potion) {
                healAmount = potion.getHealAmount();
            }
            float maxHealth = target.getMaxHealth();
            float newHealth = Math.min(target.getHealth() + healAmount, maxHealth);
            target.setHealth(newHealth);

            target.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);

            return true;
        }
        return false;
    }
}





