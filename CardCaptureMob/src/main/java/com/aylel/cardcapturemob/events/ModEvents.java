package com.aylel.cardcapturemob.events;

import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.item.FamiliarHealPotionItem;
import com.aylel.cardcapturemob.entity.custom.StrayFamiliarEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.InteractionHand;

@Mod.EventBusSubscriber(modid = CardCaptureMob.MODID)
public class ModEvents {

    // === Killcount à distance pour familiers archers ===
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof StrayFamiliarEntity stray) {
                stray.addKill();
            }
            // Ajoute ici tes autres familiers archers personnalisés :
            // if (arrow.getOwner() instanceof SkeletonArcherFamiliarEntity skeleton) skeleton.addKill();
            // etc.
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        // On check d'abord que c'est bien un craft sur une table (CraftingContainer)
        if (!(event.getInventory() instanceof CraftingContainer inv)) return;

        // Recherche la carte KO utilisée dans la grille de craft
        ItemStack foundCard = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            // On récupère la première carte qui semble KO (hp <= 0, tag CurrentHp présent)
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag().contains("CurrentHp")) {
                float hp = stack.getTag().getFloat("CurrentHp");
                if (hp <= 0f) {
                    foundCard = stack;
                    break;
                }
            }
        }
        if (!foundCard.isEmpty()) {
            ItemStack result = event.getCrafting();
            if (result != null && result.hasTag() && result.getTag().contains("CurrentHp")) {
                float newHp = result.getTag().getFloat("CurrentHp");
                // On clone tout le NBT de la carte KO
                result.setTag(foundCard.getTag().copy());
                // Puis on met à jour CurrentHp et le flag JustResurrected si besoin
                result.getTag().putFloat("CurrentHp", newHp);
                result.getTag().putBoolean("JustResurrected", true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;

        ItemStack stack = event.getItemStack();

        // Si c'est une potion de soin familier
        if (stack.getItem() instanceof FamiliarHealPotionItem potion) {
            if (!event.getLevel().isClientSide) {
                // Tente d'appliquer la logique de soin (à placer dans FamiliarHealPotionItem)
                boolean result = FamiliarHealPotionItem.tryHealFamiliar(stack, event.getEntity(), target, event.getHand());
                if (result) {
                    stack.shrink(1);
                    event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }
}






