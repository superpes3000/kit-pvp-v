package com.example.mymod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.player.Player;
import net.strauss.kitpvpmod.KitPvpMod;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID, value = Dist.CLIENT)
public class PlayerRenderHandler {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();



        if (player.isInvisible()) {
            event.setCanceled(true); // Полностью отменяем рендер игрока, включая броню и предметы
        }
    }
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;


        if (player.isInvisible()) {
            event.setCanceled(true); // Полностью отменяем рендер игрока, включая броню и предметы
        }
    }
}
