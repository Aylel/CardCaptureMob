package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.CaptureHandler;
import com.aylel.cardcapturemob.common.CardRarity;
import com.aylel.cardcapturemob.data.CardLootLoader;
import com.aylel.cardcapturemob.data.VanillaStats;
import com.aylel.cardcapturemob.entity.custom.BaseFamiliarEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.aylel.cardcapturemob.common.CardNBTHelper;
import com.aylel.cardcapturemob.common.SerialNumberManager;
import com.aylel.cardcapturemob.common.FamiliarManager;
import com.aylel.cardcapturemob.common.FamiliarData;

import java.util.UUID;

public abstract class EmptyCaptureCardItem<T extends BaseFamiliarEntity> extends Item {
    public EmptyCaptureCardItem(Item.Properties properties) {
        super(properties);
    }

    protected abstract EntityType<?> getTargetEntityType();
    protected abstract double getCaptureChance();
    protected abstract Item getFilledCardItem();
    protected abstract CardRarity getRarity();

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        if (!level.isClientSide && target.getType() == this.getTargetEntityType()) {
            if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;
            boolean success = CaptureHandler.attemptCapture(level, player, target, stack, this.getCaptureChance());
            if (success) {
                player.sendSystemMessage(Component.literal("§aCapture réussie !"));
                ItemStack filledStack = new ItemStack(this.getFilledCardItem());
                CompoundTag tag = filledStack.getOrCreateTag();
                CardNBTHelper.writeRarity(tag, getRarity());

                // -- Création du cardId à partir du nom de l'item
                String cardId = this.getDescriptionId().replace("item.cardcapturemob.", "");
                CardLootLoader.LootConfig config = CardLootLoader.get(cardId);

                int statPoints = (config != null && config.stat_points > 0)
                        ? config.stat_points
                        : getRarity().getInitialStatPoints();

                VanillaStats stats = VanillaStats.randomStats(statPoints);
                tag.put("Stats", stats.saveToNBT());

                // Pseudo & UUID
                tag.putString("OwnerName", player.getName().getString());
                tag.putUUID("OwnerUUID", player.getUUID());

                // Type de mob & numéro de série unique (avant d'ajouter la carte à l'inventaire !)
                String mobType = getTargetEntityType().toString(); // Exemple : "minecraft:wolf"
                int serial = SerialNumberManager.getNextSerial(serverLevel, mobType, getRarity());
                tag.putString("MobType", mobType);
                tag.putInt("Serial", serial);

                // UUID du familier
                UUID familiarUuid = UUID.randomUUID();
                tag.putUUID("FamiliarSerial", familiarUuid);

                // --- Création du FamiliarData (plus de level/xp !) ---
                FamiliarData familiarData = new FamiliarData(
                        familiarUuid,
                        mobType,
                        stats // VanillaStats complet
                );
                FamiliarManager.get().addFamiliar(familiarData);

                // Ajout à l'inventaire APRES avoir rempli tout le NBT
                player.addItem(filledStack);
            } else {
                player.sendSystemMessage(Component.literal("§cÉchec de la capture..."));
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}











