package net.kaupenjoe.tutorialmod.entity;

import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.entity.custom.CrowProjectile;
import net.kaupenjoe.tutorialmod.entity.custom.TomahawkProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TutorialMod.MOD_ID);


    public static final RegistryObject<EntityType<TomahawkProjectileEntity>> TOMAHAWK =
            ENTITY_TYPES.register("tomahawk", () -> EntityType.Builder.<TomahawkProjectileEntity>of(TomahawkProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("tomahawk"));

    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE =
            ENTITY_TYPES.register("grenade", () -> EntityType.Builder.<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("grenade"));

    public static final RegistryObject<EntityType<GrenadeEntity>> CROW_PROJECTILE =
            ENTITY_TYPES.register("crow_projectile", () -> EntityType.Builder.<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("crow_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}