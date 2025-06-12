package com.aylel.cardcapturemob.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import com.aylel.cardcapturemob.item.WolfCardItem;
import com.aylel.cardcapturemob.item.EmptyWolfCardItem;
import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.item.ZombieCardItem;
import com.aylel.cardcapturemob.item.EmptyZombieCaptureCardItem;
import com.aylel.cardcapturemob.item.ResurrectionScrollItem;
import com.aylel.cardcapturemob.item.FamiliarHealPotionItem;
import com.aylel.cardcapturemob.item.EmptyStrayCaptureCardItem;
import com.aylel.cardcapturemob.item.StrayCardItem;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CardCaptureMob.MODID);

    public static final RegistryObject<Item> WOLF_CARD = ITEMS.register(
            "wolf_card",
            () -> new WolfCardItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> ZOMBIE_CARD = ITEMS.register(
            "zombie_card",
            () -> new ZombieCardItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> STRAY_CARD = ITEMS.register(
            "stray_card",
            () -> new StrayCardItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> EMPTY_WOLF_CARD = ITEMS.register(
            "empty_wolf_card",
            () -> new EmptyWolfCardItem(new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Item> EMPTY_ZOMBIE_CARD = ITEMS.register(
            "empty_zombie_card",
            () -> new EmptyZombieCaptureCardItem(new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Item> EMPTY_STRAY_CARD = ITEMS.register(
            "empty_stray_card",
            () -> new EmptyStrayCaptureCardItem(new Item.Properties().stacksTo(64))
    );

    // --- Parchemins de Résurrection : utilisation de la classe custom ---
    public static final RegistryObject<Item> RESURRECTION_SCROLL_BASIC = ITEMS.register(
            "resurrection_scroll_basic",
            () -> new ResurrectionScrollItem(new Item.Properties().stacksTo(64), ResurrectionScrollItem.Type.BASIC)
    );

    public static final RegistryObject<Item> RESURRECTION_SCROLL_ADVANCED = ITEMS.register(
            "resurrection_scroll_advanced",
            () -> new ResurrectionScrollItem(new Item.Properties().stacksTo(64), ResurrectionScrollItem.Type.ADVANCED)
    );

    public static final RegistryObject<Item> RESURRECTION_SCROLL_SUPREME = ITEMS.register(
            "resurrection_scroll_supreme",
            () -> new ResurrectionScrollItem(new Item.Properties().stacksTo(64), ResurrectionScrollItem.Type.SUPREME)
    );

    // --- Potions de soin familier ---
    public static final RegistryObject<Item> MINOR_FAMILIAR_HEAL_POTION = ITEMS.register(
            "minor_familiar_heal_potion",
            () -> new FamiliarHealPotionItem(new Item.Properties().stacksTo(64), 5f)
    );

    public static final RegistryObject<Item> ADVANCED_FAMILIAR_HEAL_POTION = ITEMS.register(
            "advanced_familiar_heal_potion",
            () -> new FamiliarHealPotionItem(new Item.Properties().stacksTo(64), 10f)
    );

    public static final RegistryObject<Item> MAJOR_FAMILIAR_HEAL_POTION = ITEMS.register(
            "major_familiar_heal_potion",
            () -> new FamiliarHealPotionItem(new Item.Properties().stacksTo(64), 20f)
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static void registerCreativeTabs(IEventBus eventBus) {
        eventBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(WOLF_CARD);
                event.accept(EMPTY_WOLF_CARD);
                event.accept(ZOMBIE_CARD);
                event.accept(EMPTY_ZOMBIE_CARD);
                event.accept(STRAY_CARD);
                event.accept(EMPTY_STRAY_CARD);

                // Parchemin de resurrection
                event.accept(RESURRECTION_SCROLL_BASIC);
                event.accept(RESURRECTION_SCROLL_ADVANCED);
                event.accept(RESURRECTION_SCROLL_SUPREME);

                // Potions de soin familier
                event.accept(MINOR_FAMILIAR_HEAL_POTION);
                event.accept(ADVANCED_FAMILIAR_HEAL_POTION);
                event.accept(MAJOR_FAMILIAR_HEAL_POTION);

            }
        });
    }
}







