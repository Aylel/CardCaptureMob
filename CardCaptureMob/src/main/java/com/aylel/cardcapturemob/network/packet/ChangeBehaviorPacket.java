package com.aylel.cardcapturemob.network.packet;

import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.entity.BehaviorMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import com.aylel.cardcapturemob.entity.CardOwnerEntity;
import com.aylel.cardcapturemob.entity.BehaviorMode;


import java.util.function.Supplier;

public class ChangeBehaviorPacket {

    public ChangeBehaviorPacket() {}

    public ChangeBehaviorPacket(FriendlyByteBuf buf) {
        // Rien à lire pour ce simple packet
    }

    public void toBytes(FriendlyByteBuf buf) {
        // Rien à écrire pour ce simple packet
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            if (player == null) return;

            var world = player.level();

            var familiars = world.getEntities(player, player.getBoundingBox().inflate(64),
                    e -> e instanceof CardOwnerEntity owner &&
                            owner.getOwnerUUID() != null &&
                            owner.getOwnerUUID().equals(player.getUUID()));

            for (var entity : familiars) {
                if (entity instanceof CardOwnerEntity owner) {
                    BehaviorMode newMode = owner.getBehaviorMode().next();
                    owner.setBehaviorMode(newMode);

                    player.displayClientMessage(
                            Component.literal("[Familier] Mode changé en : " + newMode.getDisplayName()),
                            true
                    );
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}


