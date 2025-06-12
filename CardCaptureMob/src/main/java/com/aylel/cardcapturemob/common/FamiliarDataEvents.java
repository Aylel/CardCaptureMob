package com.aylel.cardcapturemob.common;

import com.aylel.cardcapturemob.data.FamiliarSaveData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;

/**
 * Handler d'événements Forge pour charger/sauvegarder automatiquement
 * les données de familiers (FamiliarManager) à l'ouverture/fermeture d'un monde serveur.
 */
@Mod.EventBusSubscriber(modid = "cardcapturemob")
public class FamiliarDataEvents {

    /**
     * Chargement des familiers à l'ouverture du monde (serveur).
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
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

    /**
     * Sauvegarde des familiers lors de la sauvegarde du monde (tick ou fermeture).
     */
    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        FamiliarManager.get().saveToWorld(serverLevel);
    }
}




