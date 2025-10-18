package net.kaupenjoe.tutorialmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Properties;

public class TripleArrowItem extends Item {

    public TripleArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            // Угол отклонения в градусах
            float[] angles = {-10f, 0f, 10f};

            for (float angle : angles) {
                Arrow arrow = new Arrow(EntityType.ARROW, world);

                // Устанавливаем позицию стрелы в глазах игрока
                arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

                // Получаем направление игрока
                Vec3 look = player.getLookAngle();

                // Применяем поворот по Y (горизонтально)
                double radians = Math.toRadians(angle);
                double sin = Math.sin(radians);
                double cos = Math.cos(radians);

                double dx = look.x * cos - look.z * sin;
                double dz = look.x * sin + look.z * cos;
                Vec3 dir = new Vec3(dx, look.y, dz);

                // Задаем скорость и разброс
                arrow.shoot(dir.x, dir.y, dir.z, 3.0f, 1.0f);


                world.addFreshEntity(arrow);
            }
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
