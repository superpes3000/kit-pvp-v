package com.example.examplemod.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagnetAttractGravity extends Item {

    public MagnetAttractGravity(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player) || level.isClientSide) return;

        double radius = 20;
        double strength = 0.015;

        // Притягиваем все LivingEntity (живые сущности) в радиусе
        level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius))
                .stream()
                .filter(e -> e != player) // сначала остальных
                .forEach(e -> attract(e, player, strength));

        // Притягиваем владельца к центру масс остальных? Или к среднему положению?
        // Если хотим, чтобы владелец тоже “двигался”, можно притянуть к ближайшей сущности
        AABB area = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != player);

        double nearestDistance = 1000;
        LivingEntity nearest = null;

        if(!nearby.isEmpty())
        {
            nearest = nearby.getFirst();
        }
        for (LivingEntity e : nearby) {
            // Вектор от сущности к игроку
            Vec3 dir = player.position().subtract(e.position());
            double distance = dir.length();
            if (distance > 0.1) {
                Vec3 velocity = dir.normalize().scale(strength);
                e.setDeltaMovement(e.getDeltaMovement().add(velocity));

                // Суммируем направление для игрока
            }
            double dist = player.position().distanceToSqr(e.position());
            if (dist < nearestDistance) {
                nearestDistance = dist;
                nearest = e;
            }
        }

        /*if (nearest != null) {
            Vec3 dir = nearest.position().subtract(player.position());

            Vec3 velocity = dir.normalize().scale(strength);

            player.setDeltaMovement(player.getDeltaMovement().add(velocity));
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту

        }
        */



        // Партиклы
        if (level.getGameTime() % 20 == 0) {
            spawnParticleCircle((ServerLevel)level, player, radius, ParticleTypes.END_ROD);
        }
    }

    private void attract(LivingEntity entity, LivingEntity target, double strength) {
        Vec3 dir = target.position().subtract(entity.position());

        Vec3 velocity = dir.normalize().scale(strength);

        entity.setDeltaMovement(entity.getDeltaMovement().add(velocity));

    }

    private void spawnParticleCircle(ServerLevel level, Player player, double radius, net.minecraft.core.particles.ParticleOptions particle) {
        int particleCount = 100; // больше частиц — плотнее круг
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = player.position().x + radius * Math.cos(angle);
            double z = player.position().z + radius * Math.sin(angle);

            // Несколько "слоёв" круга для эффекта толщины
            for (double offsetY = -0.2; offsetY <= 0.2; offsetY += 0.1) {
                double y = player.position().y + offsetY;
                level.sendParticles(
                        ParticleTypes.END_ROD, // яркие белые частицы
                        x, y, z,
                        1, // количество в каждой точке
                        0, 0, 0, 0
                );
            }
        }
    }
}
