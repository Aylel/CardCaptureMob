package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.CaptureHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public abstract class EmptyCaptureCardItem extends Item {

    public EmptyCaptureCardItem(Properties properties) {
        super(properties);
    }

    // À implémenter par chaque carte spécifique
    protected abstract EntityType<?> getTargetEntityType();
    protected abstract double getCaptureChance();
    protected abstract Item getFilledCardItem();

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();

        if (!level.isClientSide && target.getType() == getTargetEntityType()) {
            boolean success = CaptureHandler.attemptCapture(level, player, target, stack, getCaptureChance());

            if (success) {
                player.sendSystemMessage(Component.literal("§aCapture réussie !"));
                player.addItem(new ItemStack(getFilledCardItem()));
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
