package com.aylel.cardcapturemob.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CardLootLoader {

    public static class LootConfig {
        public float mob_drop_rate = 0.05f;
        public float container_drop_rate = 0.0f;
        public int minCount = 1;
        public int maxCount = 1;
        public int stat_points = -1;
        public float capture_rate = 0.0f;
    }

    private static final Map<String, LootConfig> CONFIGS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        CONFIGS.clear();
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("cardcapturemob_settings.json");

        // S'il n'existe pas, copier le template depuis le JAR (resources/config)
        if (!Files.exists(configPath)) {
            try (InputStream in = CardLootLoader.class.getClassLoader()
                    .getResourceAsStream("config/cardcapturemob_settings.json")) {
                if (in != null) {
                    Files.copy(in, configPath);
                }
            } catch (IOException ignored) {}
        }

        // Lecture du fichier
        try (FileReader reader = new FileReader(configPath.toFile())) {
            Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
            Map<String, Map<String, Object>> data = GSON.fromJson(reader, type);

            for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
                String cardId = entry.getKey();
                Map<String, Object> values = entry.getValue();

                LootConfig cfg = new LootConfig();

                if (values.containsKey("mob_drop_rate")) {
                    try { cfg.mob_drop_rate = ((Number) values.get("mob_drop_rate")).floatValue(); } catch (Exception ignored) {}
                }
                if (values.containsKey("container_drop_rate")) {
                    try { cfg.container_drop_rate = ((Number) values.get("container_drop_rate")).floatValue(); } catch (Exception ignored) {}
                }
                if (values.containsKey("min_count")) {
                    try { cfg.minCount = ((Number) values.get("min_count")).intValue(); } catch (Exception ignored) {}
                }
                if (values.containsKey("max_count")) {
                    try { cfg.maxCount = ((Number) values.get("max_count")).intValue(); } catch (Exception ignored) {}
                }
                if (values.containsKey("stat_points")) {
                    try { cfg.stat_points = ((Number) values.get("stat_points")).intValue(); } catch (Exception ignored) {}
                }
                if (values.containsKey("capture_rate")) {
                    try { cfg.capture_rate = ((Number) values.get("capture_rate")).floatValue(); } catch (Exception ignored) {}
                }

                CONFIGS.put(cardId, cfg);
            }
        } catch (IOException ignored) {}
    }

    public static LootConfig get(String cardId) {
        return CONFIGS.getOrDefault(cardId, new LootConfig());
    }

    public static Map<String, LootConfig> getAll() {
        return CONFIGS;
    }
}











