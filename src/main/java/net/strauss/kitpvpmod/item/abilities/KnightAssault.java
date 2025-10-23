package net.strauss.kitpvpmod.item.abilities;

import net.strauss.kitpvpmod.utils.EffectGiver;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class KnightAssault extends KitPvpAbility {

    public KnightAssault(Properties properties) {
        super(properties);
        setCooldown(30);
    }

    @Override
    protected void onUse(ServerLevel serverLevel, Player player, InteractionHand hand) {
        EffectGiver.applyMobEffect(player, MobEffects.MOVEMENT_SPEED, 8, 0);
        EffectGiver.applyMobEffect(player, MobEffects.DAMAGE_BOOST, 8, 0);

        serverLevel.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 1.2f);

        serverLevel.sendParticles(
                ParticleTypes.FLAME,
                player.getX(), player.getY() + 1, player.getZ(),
                20, 0.5, 1.0, 0.5, 0.05);

        player.swing(hand, true);

    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Дает силу и скорость на 8 секунд"));
        tooltip.add(Component.literal("§8Перезарядка: 30 секунд"));

    }
}
