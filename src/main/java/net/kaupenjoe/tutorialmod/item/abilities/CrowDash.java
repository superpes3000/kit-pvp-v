package net.kaupenjoe.tutorialmod.item.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CrowDash extends Item {

    public CrowDash(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }
            player.getCooldowns().addCooldown(this, 80);

            Vec3 look = player.getLookAngle();
            double dashStrength = 1.5; // сила рывка

            // Новый вектор движения — короткий "прыжок" вперёд
            Vec3 dash = new Vec3(look.x * dashStrength, look.y * dashStrength, look.z * dashStrength);

            // Устанавливаем движение игроку
            player.setDeltaMovement(dash);
            player.hurtMarked = true; // чтобы сервер отправил новое положение клиенту
        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Делает рывок вперед"));
    }

}
