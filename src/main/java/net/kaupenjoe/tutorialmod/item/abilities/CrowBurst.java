package net.kaupenjoe.tutorialmod.item.custom;

import net.kaupenjoe.tutorialmod.entity.custom.CrowProjectile;
import net.kaupenjoe.tutorialmod.item.abilities.JumpSlam;
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

public class CrowBurst extends Item {

    // Храним игроков, которые прыгают
    private static final Map<UUID, CrowBurst.JumpData> jumpingPlayers = new HashMap<>();
    private static class JumpData {
        public int delayTicks; // задержка перед проверкой приземления
        boolean hasExploded = false;

        JumpData(int delay) {
            this.delayTicks = delay;
        }
    }

    public CrowBurst(Properties properties) {
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
            player.getCooldowns().addCooldown(this, 20 * 20);
            world.playSound(null, player.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0f, 1.2f);

            var look = player.getLookAngle();
            double dashStrength = 1.5;
            Vec3 dash = new Vec3(look.x * dashStrength, 1.7f, look.z * dashStrength);

            player.setDeltaMovement(dash);
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту

            shootAround(world, player);
            player.fallDistance = 0; // сброс падения
            world.playSound(null, player.blockPosition(),
                    net.minecraft.sounds.SoundEvents.ENDER_DRAGON_FLAP,
                    net.minecraft.sounds.SoundSource.PLAYERS,
                    1.0f, 1.2f);


            // 2️⃣ Помечаем игрока как прыгающего
            jumpingPlayers.put(player.getUUID(), new CrowBurst.JumpData(20));
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level();

        if (!world.isClientSide && jumpingPlayers.containsKey(player.getUUID())) {
            CrowBurst.JumpData data = jumpingPlayers.get(player.getUUID());

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
                    world.playSound(null, player.blockPosition(),
                            net.minecraft.sounds.SoundEvents.ENDER_DRAGON_FLAP,
                            net.minecraft.sounds.SoundSource.PLAYERS,
                            1.0f, 1.2f);
                    // 2️⃣ Оглушаем всех в радиусе 3 блока
                    AABB area = new AABB(player.getX() - 3, player.getY() - 2, player.getZ() - 3,
                            player.getX() + 3, player.getY() + 2, player.getZ() + 3);

                    shootAround(world, player);

                }

                // После срабатывания удаляем из списка
                jumpingPlayers.remove(player.getUUID());
            }
        }

    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Делает прыжок. В начале и в конце"));
        tooltip.add(Component.literal("§7прыжка выстреливает плевками покругу"));
        tooltip.add(Component.literal("§8Перезарядка: 20 секунд"));
    }
    private void shootAround(Level world, Player player){
        int projectileCount = 12; // кол-во снарядов
        float radius = 1.5f;     // радиус, на котором они появляются
        float speed = 2.5f;      // скорость вылета
        float spread = 0.2f;     // разброс

        // Звук выстрела
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

        // Позиция центра (игрока)
        double px = player.getX();
        double py = player.getEyeY() - 0.2;
        double pz = player.getZ();

        // Выпускаем снаряды равномерно по кругу
        for (int i = 0; i < projectileCount; i++) {
            double angle = (2 * Math.PI / projectileCount) * i;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);

            CrowProjectile projectile = new CrowProjectile(world, player);

            // Спавним немного вокруг игрока (на радиусе)
            projectile.setPos(px + dx * radius, py, pz + dz * radius);

            // Задаём направление вылета от центра наружу
            projectile.shoot(dx, 0.1, dz, speed, spread);

            world.addFreshEntity(projectile);
        }
    }
}
