package net.kaupenjoe.tutorialmod.item.abilities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class DashItem extends Item {

    public DashItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            double dashStrength = 2.0; // сила рывка

            // Новый вектор движения — короткий "прыжок" вперёд
            Vec3 dash = new Vec3(look.x * dashStrength, look.y * dashStrength, look.z * dashStrength);

            // Устанавливаем движение игроку
            player.setDeltaMovement(dash);
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту

            // Сообщение
            player.sendSystemMessage(Component.literal("💨 Рывок вперёд!"));
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
