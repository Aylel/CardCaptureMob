package com.aylel.cardcapturemob.registry;

import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.item.EmptyWolfCardItem;
import com.aylel.cardcapturemob.item.WolfCardItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CardCaptureMob.MODID);

    public static final RegistryObject<Item> WOLF_CARD = ITEMS.register(
            "wolf_card",
            () -> new WolfCardItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> EMPTY_WOLF_CARD = ITEMS.register(
            "empty_wolf_card",
            () -> new EmptyWolfCardItem(new Item.Properties().stacksTo(1))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


