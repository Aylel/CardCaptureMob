package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.aylel.cardcapturemob.client.CardTooltipHelper;
import com.aylel.cardcapturemob.entity.custom.ZombieFamiliarEntity;
import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.UUID;

import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;
import com.aylel.cardcapturemob.util.ResurrectScrollEffect;

public abstract class SummonZombieCardItem<T extends CardOwnerEntity> extends Item {

    public SummonZombieCardItem(Properties properties) {
        super(properties);
    }

    protected abstract EntityType<? extends ZombieFamiliarEntity> getEntityType();

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
                    if (e instanceof ZombieFamiliarEntity zombie) {
                        UUID entityUuid = zombie.getFamiliarUuid();
                        if (entityUuid != null) {
                            FamiliarData entityData = FamiliarManager.get().getFamiliarData(entityUuid);
                            if (entityData != null) {
                                entityData.setCurrentHp(zombie.getHealth());
                                FamiliarManager.get().updateFamiliarData(entityData);
                            }
                        }
                    }
                    e.discard();
                }
                return InteractionResultHolder.success(stack);
            }
            // ================================================================

            // ====== AJOUT : Si carte KO, tente réanimation avec parchemin ======
            if (familiarData.getCurrentHp() <= 0) {
                ItemStack scroll = player.getOffhandItem();
                if (!scroll.isEmpty() && ResurrectScrollEffect.apply(stack, scroll)) {
                    player.sendSystemMessage(Component.literal("§aVotre familier a été réanimé !"));
                } else {
                    player.sendSystemMessage(Component.literal("Ce familier est KO. Utilisez un objet pour le soigner !"));
                }
                return InteractionResultHolder.success(stack);
            }
            // ================================================================

            ServerLevel srv = (ServerLevel) world;

            EntityType<? extends ZombieFamiliarEntity> type = getEntityType();
            ZombieFamiliarEntity ent = type.create(srv);

            if (ent != null) {
                HitResult trace = player.pick(3.0D, 0.0F, false);
                if (!(trace instanceof BlockHitResult hit)) {
                    return InteractionResultHolder.pass(stack);
                }

                BlockPos spawnPos = hit.getBlockPos().relative(hit.getDirection());
                ent.moveTo(
                        spawnPos.getX() + 0.5,
                        spawnPos.getY() + 1.0,
                        spawnPos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot()
                );
                ent.setOwnerUUID(ownerId);
                ent.setFamiliarUuid(familiarUuid);

                // --- [CORRECTION HP MAX] Applique d'abord MAX_HEALTH avant de setHealth ---
                ent.getAttribute(Attributes.MAX_HEALTH).setBaseValue(familiarData.getHp());
                ent.setHealth(familiarData.getCurrentHp());
                ent.getAttribute(Attributes.ARMOR).setBaseValue(familiarData.getArmor());
                ent.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(familiarData.getToughness());
                ent.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(familiarData.getAttack());
                ent.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(familiarData.getSpeed());

                // ---- Affichage nom et PV ----
                String baseName = ent.getName().getString();
                int pvActuels = (int) ent.getHealth();
                int pvMax = (int) ent.getMaxHealth();
                String displayName = baseName + " [" + pvActuels + "/" + pvMax + "♥]";
                ent.setCustomName(Component.literal(displayName));
                ent.setCustomNameVisible(true);

                world.addFreshEntity(ent);
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











