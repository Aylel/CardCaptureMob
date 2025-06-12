package com.aylel.cardcapturemob.registry;

import com.aylel.cardcapturemob.CardCaptureMob;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Enregistreur des recettes custom du mod.
 * Ce fichier reste prêt pour ajouter de futures recettes custom.
 */
public class ModRecipes {

    // Registre vide prêt pour des ajouts futurs
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CardCaptureMob.MODID);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
