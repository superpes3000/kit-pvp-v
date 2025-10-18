package net.kaupenjoe.tutorialmod.item.abilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BeastRush extends Item {

    // Храним активные рывки игроков: UUID -> оставшиеся тики и флаг столкновения
    private static final Map<UUID, DashData> activeDashes = new HashMap<>();

    public BeastRush(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this); // подписка на тик
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }
            player.getCooldowns().addCooldown(this, 20 * 20);

            // Ставим активный рывок: 6 секунд = 6 * 20 тиков
            activeDashes.put(player.getUUID(), new DashData(120));
            world.playSound(
                    null, // всем игрокам в радиусе
                    player.getX(), player.getY(), player.getZ(), // вокруг цели
                    SoundEvents.COW_HURT, // звук динамита
                    SoundSource.PLAYERS,
                    1.0f, // громкость
                    1.0f  // pitch
            );
            world.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0f, 1.2f);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    // Событие тика игроков
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level();

        if (!world.isClientSide && activeDashes.containsKey(player.getUUID())) {
            DashData data = activeDashes.get(player.getUUID());

            // 1️⃣ Движение вперед
            double speed = 0.8; // скорость рывка
            double dashStrength = 1;
            Vec3 look = player.getLookAngle();

            Vec3 dash = new Vec3(look.x * dashStrength, 0, look.z * dashStrength);

            // Устанавливаем движение игроку
            player.setDeltaMovement(dash);
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту

            // 2️⃣ Партиклы вокруг игрока
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        player.getX(), player.getY() + 1, player.getZ(),
                        5, 0.3, 0.5, 0.3, 0.01);
            }

            // 3️⃣ Проверка столкновений с существами в радиусе 2 блока
            AABB area = new AABB(player.getX() - 2, player.getY() - 1, player.getZ() - 2,
                    player.getX() + 2, player.getY() + 2, player.getZ() + 2);

            for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, area,
                    e -> e != player)) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 2)); // оглушение на 3 секунды
                data.hitSomeone = true;

                if (world instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                            target.getX(), target.getY() + 1, target.getZ(),
                            10, 0.5, 0.5, 0.5, 0.1);
                    world.playSound(
                            null, // всем игрокам в радиусе
                            target.getX(), target.getY(), target.getZ(), // вокруг цели
                            SoundEvents.GENERIC_EXPLODE, // звук динамита
                            SoundSource.PLAYERS,
                            1.0f, // громкость
                            1.0f  // pitch
                    );
                }


                break; // попали в кого-то — прекращаем цикл
            }
            // Если попал в кого-то — прекращаем рывок
            if (data.hitSomeone) {
                activeDashes.remove(player.getUUID());
                return; // прекращаем дальнейшее движение
            }

            // 4️⃣ Таймер
            data.ticksLeft--;
            if (data.ticksLeft <= 0) {
                if (!data.hitSomeone) {
                    // Если не попал ни в кого → сам оглушен на 3 секунды
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 2));
                }
                activeDashes.remove(player.getUUID());
            }
        }

    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Герой бежит вперед в течение 6 секунд."));
        tooltip.add(Component.literal("§7Если он врежется в кого-то, то цель оглушится на 3 секунды."));
        tooltip.add(Component.literal("§7Если за все время ни в кого не врежется оглушится сам герой"));
        tooltip.add(Component.literal("§8Перезарядка: 20 секунд"));
    }

    // Вспомогательный класс для хранения данных рывка
    private static class DashData {
        int ticksLeft;
        boolean hitSomeone = false;

        DashData(int ticks) {
            this.ticksLeft = ticks;
        }
    }
}
