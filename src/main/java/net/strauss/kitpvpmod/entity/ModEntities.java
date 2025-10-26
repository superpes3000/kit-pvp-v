package net.strauss.kitpvpmod.entity;

import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.entity.custom.TomahawkProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.strauss.kitpvpmod.entity.mob.FriendlySkeleton;
import net.strauss.kitpvpmod.entity.mob.FriendlyWitherSkeleton;
import net.strauss.kitpvpmod.entity.projectile.HookProjectile;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KitPvpMod.MOD_ID);


    public static final RegistryObject<EntityType<TomahawkProjectileEntity>> TOMAHAWK =
            ENTITY_TYPES.register("tomahawk", () -> EntityType.Builder.<TomahawkProjectileEntity>of(TomahawkProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("tomahawk"));

    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE =
            ENTITY_TYPES.register("grenade", () -> EntityType.Builder.<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("grenade"));

    public static final RegistryObject<EntityType<GrenadeEntity>> CROW_PROJECTILE =
            ENTITY_TYPES.register("crow_projectile", () -> EntityType.Builder.<GrenadeEntity>of(GrenadeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.8f).build("crow_projectile"));

    public static final RegistryObject<EntityType<HookProjectile>> HOOK_PROJECTILE =
            ENTITY_TYPES.register("hook_projectile", () -> EntityType.Builder.<HookProjectile>of(HookProjectile::new, MobCategory.MISC)
                    .clientTrackingRange(128)
                    .sized(0.8f, 0.8f).build("hook_projectile"));

    public static final RegistryObject<EntityType<FriendlySkeleton>> FRIENDLY_SKELETON =
            ENTITY_TYPES.register("friendly_skeleton",
                    () -> EntityType.Builder.<FriendlySkeleton>of(FriendlySkeleton::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.99f).build("friendly_skeleton"));

    public static final RegistryObject<EntityType<FriendlyWitherSkeleton>> FRIENDLY_WITHER_SKELETON =
            ENTITY_TYPES.register("friendly_wither_skeleton",
                    () -> EntityType.Builder.<FriendlyWitherSkeleton>of(FriendlyWitherSkeleton::new, MobCategory.MONSTER)
                            .sized(0.7f, 2.4f).build("friendly_wither_skeleton"));
    
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}