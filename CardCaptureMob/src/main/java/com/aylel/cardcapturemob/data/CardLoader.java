package com.aylel.cardcapturemob.data;

import com.aylel.cardcapturemob.cards.CardDefinition;
import com.google.gson.Gson;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class CardLoader {
    /** L’unique instance à utiliser partout */
    public static final CardLoader INSTANCE = new CardLoader();

    /** Le conteneur de toutes les cartes chargées */
    private final Map<String, CardDefinition> cards = new HashMap<>();

    /** Ne pas instancier en-dehors de cette classe */
    private CardLoader() {}

    /** Chargement (ou rechargement) des JSON de cartes depuis config/cardcapturemob/cards */
    public void load() {
        cards.clear();
        Gson gson = new Gson();
        Path folder = FMLPaths.GAMEDIR.get().resolve("config/cardcapturemob/cards");

        try {
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            Files.list(folder)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(path))) {
                            CardDefinition card = gson.fromJson(reader, CardDefinition.class);
                            cards.put(card.id, card);
                            // Suppression du print debug ici
                        } catch (Exception e) {
                            System.err.println("❌ Erreur en lisant la carte : " + path.getFileName());
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des cartes : ");
            e.printStackTrace();
        }
    }

    public CardDefinition getDefinition(String id) {
        return cards.get(id);
    }

    public static CardDefinition getDefinitionById(String id) {
        return INSTANCE.getDefinition(id);
    }

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        INSTANCE.load();
    }
}



