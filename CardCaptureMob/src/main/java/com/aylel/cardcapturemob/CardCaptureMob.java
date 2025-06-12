package com.aylel.cardcapturemob;

import com.aylel.cardcapturemob.registry.ModEntities;
import com.aylel.cardcapturemob.registry.ModItems;
import com.aylel.cardcapturemob.data.FamiliarSaveData;
import com.aylel.cardcapturemob.common.FamiliarManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.server.level.ServerLevel;
import com.aylel.cardcapturemob.network.ModPackets;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import com.aylel.cardcapturemob.loot.MobDropHandler;

@Mod(CardCaptureMob.MODID)
public class CardCaptureMob {

    public static final String MODID = "cardcapturemob";

    public CardCaptureMob() {
        System.out.println("=== DEBUG : Nouvelle compilation chargée ===");
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(bus);
        ModItems.register(bus);
        ModPackets.register();
        ModItems.registerCreativeTabs(bus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(MobDropHandler.class);

        bus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            com.aylel.cardcapturemob.data.CardLootLoader.load();
        });
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        FamiliarSaveData saveData = serverLevel.getDataStorage().computeIfAbsent(
                FamiliarSaveData::load,
                FamiliarSaveData::new,
                FamiliarSaveData.NAME
        );
        FamiliarManager.get().setCurrentSaveData(saveData);
        FamiliarManager.get().reloadFromSaveData(saveData);
    }

    @SubscribeEvent
    public void onWorldSave(LevelEvent.Save event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        FamiliarManager.get().saveToWorld(serverLevel);
    }

}






