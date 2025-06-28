package com.aylel.cardcapturemob.client.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeyMappings {

    public static final String CATEGORY = "key.cardcapturemob.category";
    public static KeyMapping CHANGE_MODE;

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        CHANGE_MODE = new KeyMapping(
                "key.cardcapturemob.change_mode",
                GLFW.GLFW_KEY_M, // touche par défaut
                CATEGORY
        );
        event.register(CHANGE_MODE);
    }
}
