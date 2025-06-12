package com.aylel.cardcapturemob.network;

import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.network.packet.ChangeBehaviorPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.Optional;

public class ModPackets {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CardCaptureMob.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++,
                ChangeBehaviorPacket.class,
                ChangeBehaviorPacket::toBytes,
                ChangeBehaviorPacket::new,
                ChangeBehaviorPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
