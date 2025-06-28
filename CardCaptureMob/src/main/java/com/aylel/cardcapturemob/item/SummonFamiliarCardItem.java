package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.client.CardTooltipHelper;
import com.aylel.cardcapturemob.entity.CardInstance;
import com.aylel.cardcapturemob.entity.custom.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.entity.custom.WolfFamiliarEntity;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.ai.attributes.Attributes;

import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;
import com.aylel.cardcapturemob.util.ResurrectScrollEffect;

public abstract class SummonFamiliarCardItem<T extends CardOwnerEntity> extends Item {

    public SummonFamiliarCardItem(Properties properties) {
        super(properties);
    }

    protected abstract EntityType<?> getEntityType();
    protected abstract CardDefinition getCardDefinition();

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        CompoundTag root = stack.getOrCreateTag();

        // ====== Récupère l'UUID et FamiliarData ======
        UUID familiarUuid = null;
        FamiliarData familiarData = null;
        if (root.hasUUID("FamiliarSerial")) {
            familiarUuid = root.getUUID("FamiliarSerial");
            familiarData = FamiliarManager.get().getFamiliarData(familiarUuid);
            if (familiarData == null) {
                player.sendSystemMessage(Component.literal("§cImpossible de retrouver les données du familier (UUID inconnu)."));
                return InteractionResultHolder.fail(stack);
            }
        } else {
            player.sendSystemMessage(Component.literal("§cErreur : carte sans UUID de familier !"));
            return InteractionResultHolder.fail(stack);
        }
        // ============================================

        // ==== Gestion de la résurrection avec parchemin en offhand ====
        ItemStack offhandStack = player.getOffhandItem();
        if (familiarData.getCurrentHp() <= 0 && !offhandStack.isEmpty()) {
            if (ResurrectScrollEffect.apply(stack, offhandStack)) {
                player.sendSystemMessage(Component.literal("§aVotre familier a été réanimé !"));
                return InteractionResultHolder.success(stack);
            }
        }
        // ==============================================================

        if (!world.isClientSide) {
            UUID ownerId = player.getUUID();

            // ==== Désinvocation (ranger le familier existant si présent) ====
            List<Entity> existing = world.getEntities(
                    player,
                    player.getBoundingBox().inflate(64),
                    e -> e instanceof CardOwnerEntity owner && owner.getOwnerUUID().equals(ownerId)
            );
            if (!existing.isEmpty()) {
                for (Entity e : existing) {
                    if (e instanceof WolfFamiliarEntity wolf) {
                        UUID entityUuid = wolf.getFamiliarUuid();
                        if (entityUuid != null) {
                            FamiliarData entityData = FamiliarManager.get().getFamiliarData(entityUuid);
                            if (entityData != null) {
                                entityData.setCurrentHp(wolf.getHealth());
                                FamiliarManager.get().updateFamiliarData(entityData);
                            }
                        }
                    }
                    e.discard();
                }
                return InteractionResultHolder.success(stack);
            }
            // ================================================================

            // ---- Interdit d'invoquer si KO ----
            if (familiarData.getCurrentHp() <= 0) {
                player.sendSystemMessage(Component.literal("Ce familier est KO. Utilisez un objet pour le soigner !"));
                return InteractionResultHolder.success(stack);
            }

            ServerLevel srv = (ServerLevel) world;

            Entity ent = getEntityType().create(srv);
            if (ent instanceof WolfFamiliarEntity wolf) {
                HitResult trace = player.pick(3.0D, 0.0F, false);
                if (!(trace instanceof BlockHitResult hit)) {
                    return InteractionResultHolder.pass(stack);
                }

                BlockPos spawnPos = hit.getBlockPos().relative(hit.getDirection());
                wolf.moveTo(
                        spawnPos.getX() + 0.5,
                        spawnPos.getY() + 1.0,
                        spawnPos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot()
                );
                wolf.setOwnerUUID(ownerId);
                wolf.setFamiliarUuid(familiarUuid);

                // --- [CORRECTION HP MAX] Applique d'abord MAX_HEALTH avant de setHealth ---
                wolf.getAttribute(Attributes.MAX_HEALTH).setBaseValue(familiarData.getHp());
                wolf.setHealth(familiarData.getCurrentHp());
                wolf.getAttribute(Attributes.ARMOR).setBaseValue(familiarData.getArmor());
                wolf.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(familiarData.getToughness());
                wolf.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(familiarData.getAttack());
                wolf.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(familiarData.getSpeed());

                // ---- Affichage nom et PV ----
                String baseName = wolf.getName().getString();
                int pvActuels = (int) wolf.getHealth();
                int pvMax = (int) wolf.getMaxHealth();
                String displayName = baseName + " [" + pvActuels + "/" + pvMax + "♥]";
                wolf.setCustomName(Component.literal(displayName));
                wolf.setCustomNameVisible(true);

                world.addFreshEntity(wolf);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        CardTooltipHelper.appendStatsAndLevel(stack, world, tooltip, flag);
    }
}











