package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.strauss.kitpvpmod.entity.ModEntities;
import net.strauss.kitpvpmod.entity.mob.FriendlySkeleton;
import net.strauss.kitpvpmod.utils.EffectGiver;

import java.util.ArrayList;
import java.util.List;

public class SkeletonEscape extends KitPvpAbility {
    public SkeletonEscape(Properties pProperties) {
        super(pProperties);
        setCooldown(20);
    }

    @Override
    protected void onUse(ServerLevel level, Player player, InteractionHand hand) {
        EffectGiver.applyMobEffect(player, MobEffects.INVISIBILITY, 4, 0);
        EffectGiver.applyMobEffect(player, MobEffects.MOVEMENT_SPEED, 4, 1);

        if(!level.isClientSide()) {
            var archers = new ArrayList<FriendlySkeleton>();
            archers.add(new FriendlySkeleton(ModEntities.FRIENDLY_SKELETON.get(), level, player));
            archers.add(new FriendlySkeleton(ModEntities.FRIENDLY_SKELETON.get(), level, player));

            for (FriendlySkeleton archer: archers) {
                archer.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0f);
                archer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
                level.addFreshEntity(archer);
            }
        }

        player.swing(hand, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Дает невидимость и скорость на 4 секунды"));
        tooltip.add(Component.literal("§8Перезарядка: 20 секунд"));
    }
}

