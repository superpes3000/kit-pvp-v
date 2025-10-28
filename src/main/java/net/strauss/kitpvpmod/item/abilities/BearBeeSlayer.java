package net.strauss.kitpvpmod.item;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.strauss.kitpvpmod.entity.mob.BearAggressiveBee;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;

public class BearBeeSlayer extends Item {

    private static final int RADIUS = 10;

    public BearBeeSlayer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }

            player.getCooldowns().addCooldown(this, 20 * 20);

            // Находим ближайшие 2 пчелы BearAggressiveBee
            AABB aabb = new AABB(player.getX() - RADIUS, player.getY() - 5, player.getZ() - RADIUS,
                    player.getX() + RADIUS, player.getY() + 5, player.getZ() + RADIUS);

            List<BearAggressiveBee> bees = level.getEntitiesOfClass(BearAggressiveBee.class, aabb);
            bees.sort(Comparator.comparingDouble(b -> b.distanceToSqr(player))); // сортировка по расстоянию

            int killed = 0;
            for (BearAggressiveBee bee : bees) {
                if (killed >= 2) break;
                bee.discard(); // убиваем пчелу
                killed++;
            }
        } else {
            // Клиентская визуализация круга радиусом 10 блоков
            for (int i = 0; i < 50; i++) {
                double angle = Math.random() * Math.PI * 2;
                double r = Math.random() * RADIUS;
                double offsetX = Math.cos(angle) * r;
                double offsetZ = Math.sin(angle) * r;
                double offsetY = 0.1;

                // Жёлтые частицы
                level.addParticle(new DustParticleOptions(new Vector3f(1f, 1f, 0f), 1f),
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        0, 0, 0);
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Вы убиваете 2 ближайшие пчелы в радиусе 10 блоков."));
        tooltip.add(Component.literal("§7Спавнит 2 агрессивные пчелы."));

    }
}
