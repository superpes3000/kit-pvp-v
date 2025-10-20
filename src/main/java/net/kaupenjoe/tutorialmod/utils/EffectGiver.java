package net.kaupenjoe.tutorialmod.utils;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class EffectGiver {
    public static void applyMobEffect(Player player,   net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect, int duration, int levelFromZero){
        player.addEffect(new MobEffectInstance(effect, 20 * duration, levelFromZero));
    }
}
