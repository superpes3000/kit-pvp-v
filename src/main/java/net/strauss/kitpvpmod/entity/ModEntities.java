package net.strauss.kitpvpmod.entity;

import net.minecraft.resources.ResourceLocation;
import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.entity.custom.TomahawkProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.strauss.kitpvpmod.entity.mob.*;
import net.strauss.kitpvpmod.entity.mob.BearHoneyEntity;
import net.strauss.kitpvpmod.entity.mob.RideableSlimeEntity;
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

    public static final RegistryObject<EntityType<BearAggressiveBee>> BEAR_AGGRESSIVE_BEE =
            ENTITY_TYPES.register("bear_aggressive_bee",
                    () -> EntityType.Builder.<BearAggressiveBee>of(BearAggressiveBee::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.6f).build("bear_aggressive_bee"));

    public static final RegistryObject<EntityType<BearHoneyEntity>> BEAR_HONEY_ENTITY =
            ENTITY_TYPES.register("bear_honey_entity", () -> EntityType.Builder.<BearHoneyEntity>of(BearHoneyEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("bear_honey_entity"));
    public static final RegistryObject<EntityType<BearHoneySnowballEntity>> BEAR_HONEY_PROJECTILE =
            ENTITY_TYPES.register("bear_honey_projectile", () -> EntityType.Builder.<BearHoneySnowballEntity>of(BearHoneySnowballEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("bear_honey_projectile"));

    public static final RegistryObject<EntityType<SwordProjectileEntity>> SWORD_PROJECTILE =
            ENTITY_TYPES.register("sword_projectile", () -> EntityType.Builder.<SwordProjectileEntity>of(SwordProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("sword_projectile"));

    public static final RegistryObject<EntityType<net.strauss.kitpvpmod.entity.mob.RideableSlimeEntity>> RIDEABLE_SLIME =
            ENTITY_TYPES.register("rideable_slime",
                    () -> EntityType.Builder.<RideableSlimeEntity>of(RideableSlimeEntity::new, MobCategory.MISC)
                            .sized(1.5f, 1.5f)
                            .build("rideable_slime"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}