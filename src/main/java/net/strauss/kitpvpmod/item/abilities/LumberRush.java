package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LumberRush extends Item {
    public LumberRush(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Проверка кулдауна
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!world.isClientSide) {
            // 1️⃣ Даем эффект скорости 2 и спешки на 5 секунд (100 тиков)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0));

            // 2️⃣ Устанавливаем кулдаун предмета 20 секунд (400 тиков)
            player.getCooldowns().addCooldown(this, 400);

            // 3️⃣ Звук активации
            world.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0f, 1.2f);

            // 4️⃣ Первичная вспышка партиклов
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        player.getX(), player.getY() + 1, player.getZ(),
                        20, 0.5, 1.0, 0.5, 0.05
                );
            }
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Дает скорость и спешку на 6 секунд"));
        tooltip.add(Component.literal("§8Перезарядка: 20 секунд"));
    }
}
