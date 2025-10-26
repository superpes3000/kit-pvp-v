package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.item.MinecraftColor;
import net.strauss.kitpvpmod.utils.EffectGiver;

import javax.swing.*;
import java.util.List;

public class KnightEstus extends Item{

        protected int Cooldown = 15;
        private boolean isCooldownSet;
        public KnightEstus(Properties pProperties) {
            super(pProperties);
        }


        public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

            if(!world.isClientSide){
                ItemStack stack = player.getItemInHand(hand);
                if (player.getCooldowns().isOnCooldown(this)) {

                    return InteractionResultHolder.fail(stack);
                }
                player.getCooldowns().addCooldown(this, 500);
                player.heal(4.0F); // 1 сердце = 2 HP
                EffectGiver.applyMobEffect(player, MobEffects.MOVEMENT_SPEED, 5, 0);
                // Убираем 1 предмет из стека
                stack.shrink(1);
            }


            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
            // Добавляем описание
            tooltip.add(Component.literal("§7Хилит и дает скорость на 5 секунд"));
        }

}
