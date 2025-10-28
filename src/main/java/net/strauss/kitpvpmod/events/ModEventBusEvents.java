package net.strauss.kitpvpmod.events;

import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.client.TomahawkProjectileModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.strauss.kitpvpmod.entity.ModEntities;
import net.strauss.kitpvpmod.entity.mob.BearAggressiveBee;
import net.strauss.kitpvpmod.entity.mob.FriendlySkeleton;
import net.strauss.kitpvpmod.entity.mob.FriendlyWitherSkeleton;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TomahawkProjectileModel.LAYER_LOCATION, TomahawkProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {

    }

    @SubscribeEvent
    public static void onEntityAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.FRIENDLY_SKELETON.get(),
                FriendlySkeleton.createAttributes().build()
        );
        event.put(
                ModEntities.FRIENDLY_WITHER_SKELETON.get(),
                FriendlyWitherSkeleton.createAttributes().build()
        );
        event.put(
                ModEntities.BEAR_AGGRESSIVE_BEE.get(),
                BearAggressiveBee.createAttributes().build()
        );
    }
}