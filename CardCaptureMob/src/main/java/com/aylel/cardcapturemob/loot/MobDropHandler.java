package com.aylel.cardcapturemob.loot;

import com.aylel.cardcapturemob.data.CardLootLoader;
import com.aylel.cardcapturemob.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Map;
import java.util.Random;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import static com.aylel.cardcapturemob.CardCaptureMob.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobDropHandler {

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onMobKilled(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (entity instanceof Player) return; // Ne pas dropper sur les joueurs

        for (Map.Entry<String, CardLootLoader.LootConfig> entry : CardLootLoader.getAll().entrySet()) {
            String lootId = entry.getKey();
            CardLootLoader.LootConfig config = entry.getValue();

            float randomValue = RANDOM.nextFloat();

            if (randomValue < config.mob_drop_rate) {
                int count = config.minCount + RANDOM.nextInt(config.maxCount - config.minCount + 1);
                ItemStack stack = getLootItem(lootId, count);

                if (!stack.isEmpty()) {
                    event.getDrops().add(new ItemEntity(
                            entity.level(),
                            entity.getX(), entity.getY(), entity.getZ(),
                            stack
                    ));
                }
            }
        }
    }

    private static ItemStack getLootItem(String lootId, int count) {
        // Si tu ajoutes de nouveaux items dans ModItems, adapte ce switch !
        return switch (lootId) {
            case "empty_zombie_card" -> new ItemStack(ModItems.EMPTY_ZOMBIE_CARD.get(), count);
            case "empty_wolf_card"   -> new ItemStack(ModItems.EMPTY_WOLF_CARD.get(), count);
            case "resurrection_scroll_basic" -> new ItemStack(ModItems.RESURRECTION_SCROLL_BASIC.get(), count);
            case "resurrection_scroll_advanced" -> new ItemStack(ModItems.RESURRECTION_SCROLL_ADVANCED.get(), count);
            case "resurrection_scroll_supreme" -> new ItemStack(ModItems.RESURRECTION_SCROLL_SUPREME.get(), count);
            // Ajoute ici tes futurs objets spéciaux…
            default -> {
                // Fallback universel : essaye de récupérer l'item via le registry Forge (si bien déclaré dans ModItems)
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, lootId));
                yield (item != null) ? new ItemStack(item, count) : ItemStack.EMPTY;
            }
        };
    }
}




