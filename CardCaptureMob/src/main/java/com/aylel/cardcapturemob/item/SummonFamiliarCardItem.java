package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.cards.CardDefinition;
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

import java.util.List;
import java.util.UUID;

public abstract class SummonFamiliarCardItem extends Item {

    public SummonFamiliarCardItem(Properties properties) {
        super(properties);
    }

    /** Le type d'entité que cette carte invoque */
    protected abstract EntityType<? extends BaseFamiliarEntity> getEntityType();

    /** La définition de carte associée */
    protected abstract CardDefinition getCardDefinition();

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Ne faire le spawn/recup qu’en serveur
        if (!world.isClientSide) {
            UUID ownerId = player.getUUID();

            // 1) Si un familier existe déjà, on le récupère dans la carte
            List<Entity> existing = world.getEntities(
                    player,
                    player.getBoundingBox().inflate(64),
                    e -> e instanceof BaseFamiliarEntity && ((BaseFamiliarEntity)e).getOwnerUUID().equals(ownerId)
            );
            if (!existing.isEmpty()) {
                for (Entity e : existing) {
                    BaseFamiliarEntity fam = (BaseFamiliarEntity)e;
                    CompoundTag data = fam.saveInstanceToNBT();
                    stack.getOrCreateTag().put("CardData", data);
                    fam.discard();
                }
                return InteractionResultHolder.success(stack);
            }

            // 2) Sinon on invoque un nouveau familier
            CardDefinition def = getCardDefinition();
            CompoundTag savedData = stack.getOrCreateTag().getCompound("CardData");
            CardInstance instance = new CardInstance(def, savedData);

            ServerLevel srv = (ServerLevel) world;
            BaseFamiliarEntity fam = getEntityType().create(srv);
            if (fam != null) {
                // on trace un rayon devant le joueur
                HitResult trace = player.pick(3.0D, 0.0F, false);

                // on ne traite que les block-hits
                if (!(trace instanceof BlockHitResult hit)) {
                    // pas de bloc sous le curseur → on laisse Android passer
                    return InteractionResultHolder.pass(stack);
                }

                BlockPos spawnPos = hit.getBlockPos().relative(hit.getDirection());
                fam.moveTo(
                        spawnPos.getX() + 0.5,
                        spawnPos.getY() + 1.0,
                        spawnPos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot()
                );
                fam.setOwnerUUID(ownerId);
                fam.loadInstanceFromNBT(instance);
                world.addFreshEntity(fam);

                // on consomme la carte en survie
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            // fin du « !world.isClientSide »
        }

        // Toujours renvoyer sidedSuccess pour signaler qu’on a consommé l’action
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

}


