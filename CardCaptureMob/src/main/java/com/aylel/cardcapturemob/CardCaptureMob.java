package com.aylel.cardcapturemob;

import com.aylel.cardcapturemob.registry.ModEntities;
import com.aylel.cardcapturemob.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CardCaptureMob.MODID)
public class CardCaptureMob {
    public static final String MODID = "cardcapturemob";

    public CardCaptureMob() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Enregistrement des entités et items
        ModEntities.register(bus);
        ModItems.register(bus);
    }
}
