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

public class JumpSlam extends Item {

    // Храним игроков, которые прыгают
    private static final Map<UUID, JumpData> jumpingPlayers = new HashMap<>();
    private static class JumpData {
        public int delayTicks; // задержка перед проверкой приземления
        boolean hasExploded = false;

        JumpData(int delay) {
            this.delayTicks = delay;
        }
    }

    public JumpSlam(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {

            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }
            player.getCooldowns().addCooldown(this, 200);
            world.playSound(null, player.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0f, 1.2f);

            var look = player.getLookAngle();
            double dashStrength = 1;
            Vec3 dash = new Vec3(look.x * dashStrength, 0.6f, look.z * dashStrength);

            player.setDeltaMovement(dash);
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту


            player.fallDistance = 0; // сброс падения
            world.playSound(
                    null, // всем игрокам в радиусе
                    player.getX(), player.getY(), player.getZ(), // вокруг цели
                    SoundEvents.DRAGON_FIREBALL_EXPLODE, // звук динамита
                    SoundSource.PLAYERS,
                    0.3f, // громкость
                    1.2f  // pitch
            );


            // 2️⃣ Помечаем игрока как прыгающего
            jumpingPlayers.put(player.getUUID(), new JumpData(20));
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level();

        if (!world.isClientSide && jumpingPlayers.containsKey(player.getUUID())) {
            JumpData data = jumpingPlayers.get(player.getUUID());

            // 3️⃣ Отсчитываем задержку
            if (data.delayTicks > 0) {
                data.delayTicks--;
                return; // пока не прошло время — не проверяем приземление
            }
            // Проверяем приземление
            if (!data.hasExploded && player.onGround()) {
                // Игрок только что приземлился
                data.hasExploded = true;

                if (world instanceof ServerLevel serverLevel) {
                    // 1️⃣ Партиклы взрыва

                    serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            player.getX(), player.getY() + 1, player.getZ(),
                            5, 0.3, 0.5, 0.3, 0.01);
                    world.playSound(
                            null, // всем игрокам в радиусе
                            player.getX(), player.getY(), player.getZ(), // вокруг цели
                            SoundEvents.ANVIL_FALL, // звук динамита
                            SoundSource.PLAYERS,
                            0.5f, // громкость
                            0.7f  // pitch
                    );
                    // 2️⃣ Оглушаем всех в радиусе 3 блока
                    AABB area = new AABB(player.getX() - 3, player.getY() - 2, player.getZ() - 3,
                            player.getX() + 3, player.getY() + 2, player.getZ() + 3);

                    for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, area, e -> e != player)) {
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));

                    }

            }

                // После срабатывания удаляем из списка
                jumpingPlayers.remove(player.getUUID());
            }
        }

    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Делает прыжок и после приземления"));

        tooltip.add(Component.literal("§7оглушает всех в радиусе 3 блоков"));
        tooltip.add(Component.literal("§8Перезарядка: 10 секунд"));
    }
}
