package com.example.flashbangmod.registry;

import com.example.flashbangmod.FlashbangMod;
import com.example.flashbangmod.entities.FlashbangEntity;
import net.kaupenjoe.tutorialmod.FlashbangEntity;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<EntityType<net.kaupenjoe.tutorialmod.FlashbangEntity>> FLASHBANG =
            ENTITY_TYPES.register("flashbang", () ->
                    EntityType.Builder.of(net.kaupenjoe.tutorialmod.FlashbangEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("flashbang") // просто строка id
            );
}
