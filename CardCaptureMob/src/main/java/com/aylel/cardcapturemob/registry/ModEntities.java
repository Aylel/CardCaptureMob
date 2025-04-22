package com.aylel.cardcapturemob.registry;


import com.aylel.cardcapturemob.CardCaptureMob;
import com.aylel.cardcapturemob.entity.custom.WolfFamiliarEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;



@EventBusSubscriber(modid = CardCaptureMob.MODID, bus = EventBusSubscriber.Bus.MOD)


public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CardCaptureMob.MODID);

    public static final RegistryObject<EntityType<WolfFamiliarEntity>> WOLF_FAMILIAR =
            ENTITIES.register("wolf_familiar",
                    () -> EntityType.Builder.of(WolfFamiliarEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(CardCaptureMob.MODID, "wolf_familiar").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(WOLF_FAMILIAR.get(), Wolf.createAttributes().build());
    }
}
