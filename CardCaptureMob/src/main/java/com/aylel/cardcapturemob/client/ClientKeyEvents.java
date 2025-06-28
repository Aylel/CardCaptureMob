package com.aylel.cardcapturemob.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.aylel.cardcapturemob.network.ModPackets;
import com.aylel.cardcapturemob.network.packet.ChangeBehaviorPacket;
import com.aylel.cardcapturemob.client.keybinds.ModKeyMappings;


@Mod.EventBusSubscriber(modid = "cardcapturemob", value = Dist.CLIENT)
public class ClientKeyEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyMappings.CHANGE_MODE.consumeClick()) {
            // Quand le joueur appuie sur la touche, envoyer un packet
            ModPackets.sendToServer(new ChangeBehaviorPacket());
        }
    }
}
