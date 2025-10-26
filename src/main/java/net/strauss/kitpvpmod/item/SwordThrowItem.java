package com.example.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.SwordProjectileEntity;

import java.util.List;

public class SwordThrowItem extends Item {
    public SwordThrowItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(stack);
            }
            player.setInvisible(false);
            // создаём сущность и задаём скорость/направление
            player.getCooldowns().addCooldown(stack.getItem(), 140);

            SwordProjectileEntity proj = new SwordProjectileEntity(level, player, stack.copy());
            // метод shootFromRotation / shoot — в зависимости от мэппинга, общий вариант:
            proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6f, 1.0f);
            // выставляем позицию чуть перед игроком, чтобы не бить себя
            proj.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            level.addFreshEntity(proj);

        }

        // анимация рукопожатия/использования — SUCCESS чтобы сработал swing
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Можно кинуть. "));
        tooltip.add(Component.literal("§7При попадании наносит большой урон и оглушает."));

    }
}
