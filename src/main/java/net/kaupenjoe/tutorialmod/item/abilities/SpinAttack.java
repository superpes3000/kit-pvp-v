package com.example.mod.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class SpinAttack extends Item {
    private double radius = 6.0;
    private float damage = 3.0f;
    public SpinAttack(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {

            Vec3 playerPos = player.position();

            // Зона поиска существ
            AABB area = new AABB(
                    playerPos.x - radius, playerPos.y - radius, playerPos.z - radius,
                    playerPos.x + radius, playerPos.y + radius, playerPos.z + radius
            );

            // Получаем всех существ в радиусе
            List<Entity> nearby = world.getEntities(player, area, e -> e instanceof LivingEntity && e != player);

            for (Entity entity : nearby) {
                // Наносим урон
                entity.hurt(world.damageSources().playerAttack(player), damage);

                // Вектор от моба к игроку
                Vec3 dir = player.position().subtract(entity.position()).normalize().scale(1.2);

                // Придаём скорость к игроку (притягивание)
                entity.setDeltaMovement(entity.getDeltaMovement().add(dir));
            }

            // Можно добавить звук
            world.playSound(null, player.blockPosition(),
                    net.minecraft.sounds.SoundEvents.ENDER_DRAGON_FLAP,
                    net.minecraft.sounds.SoundSource.PLAYERS,
                    1.0f, 1.2f);

            ServerLevel serverLevel = (ServerLevel) world;


            int particleCount = 150; // больше частиц — плотнее круг
            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double x = playerPos.x + radius * Math.cos(angle);
                double z = playerPos.z + radius * Math.sin(angle);

                // Несколько "слоёв" круга для эффекта толщины
                for (double offsetY = -0.2; offsetY <= 0.4; offsetY += 0.2) {
                    double y = playerPos.y + offsetY;
                    serverLevel.sendParticles(
                            ParticleTypes.END_ROD, // яркие белые частицы
                            x, y, z,
                            2, // количество в каждой точке
                            0, 0, 0, 0
                    );
                }
            }
            player.getCooldowns().addCooldown(this, 160);
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Притягивает и наносит урон"));
        tooltip.add(Component.literal("§7всем в радиусе " + radius + " блоков"));

        tooltip.add(Component.literal("§8Перезарядка: 8 секунд"));
        tooltip.add(Component.literal("§8Урон: " + damage));
    }

}
