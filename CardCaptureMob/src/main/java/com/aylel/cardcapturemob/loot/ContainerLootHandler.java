package com.aylel.cardcapturemob.loot;

import com.aylel.cardcapturemob.data.CardLootLoader;
import com.aylel.cardcapturemob.data.CardLootLoader.LootConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

/**
 * Handler pour ajouter TOUT loot (cartes, parchemins, potions...) dans les coffres/barils vanilla.
 * Se base sur les taux de drop de la config JSON (clé "container_drop_rate").
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ContainerLootHandler {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        String lootTable = event.getName().toString();

        // On cible uniquement les loot tables des coffres/barils vanilla (adapte si tu ajoutes d'autres structures)
        if (!lootTable.contains("chests") && !lootTable.contains("barrel")) return;

        for (Map.Entry<String, LootConfig> entry : CardLootLoader.getAll().entrySet()) {
            String lootId = entry.getKey(); // ex: "empty_wolf_card", "resurrection_scroll_basic"
            LootConfig config = entry.getValue();

            // On vérifie la présence du taux de drop container pour cet item
            if (config.container_drop_rate <= 0f) continue;

            ResourceLocation itemRL = new ResourceLocation("cardcapturemob", lootId);
            Item item = ForgeRegistries.ITEMS.getValue(itemRL);
            if (item == null) continue;

            LootPool pool = LootPool.lootPool()
                    .add(LootItem.lootTableItem(item)
                            .when(LootItemRandomChanceCondition.randomChance((float) config.container_drop_rate)))
                    .name("ccm_" + lootId)
                    .build();

            event.getTable().addPool(pool);
        }
    }
}





