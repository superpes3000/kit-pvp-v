package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.ModEntities;
import net.strauss.kitpvpmod.entity.mob.BearAggressiveBee;
import net.strauss.kitpvpmod.utils.EffectGiver;

import java.util.List;

public class BearHoneyComb extends Item{


    public BearHoneyComb(Properties pProperties) {
            super(pProperties);
        }


        public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

            if(!world.isClientSide){
                ItemStack stack = player.getItemInHand(hand);
                if (player.getCooldowns().isOnCooldown(this)) {

                    return InteractionResultHolder.fail(stack);
                }
                player.getCooldowns().addCooldown(this, 200);
                player.heal(6.0F); // 1 сердце = 2 HP

                var aggressiveBee = new BearAggressiveBee(ModEntities.BEAR_AGGRESSIVE_BEE.get(), world, player);
                var aggressiveBee2 = new BearAggressiveBee(ModEntities.BEAR_AGGRESSIVE_BEE.get(), world, player);

                aggressiveBee.moveTo(player.getX() - 1, player.getY(), player.getZ(), player.getYRot(), 0f);
                aggressiveBee2.moveTo(player.getX() + 1, player.getY(), player.getZ(), player.getYRot(), 0f);


                world.addFreshEntity(aggressiveBee);
                world.addFreshEntity(aggressiveBee2);


                stack.shrink(1);
            }


            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
            // Добавляем описание
            tooltip.add(Component.literal("§7Хилит и спавнит 2 агрессивные пчелы"));
        }

}
