package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.mob.FriendlySkeleton;
import net.strauss.kitpvpmod.entity.mob.FriendlyWitherSkeleton;
import net.strauss.kitpvpmod.entity.ModEntities;

import java.util.List;

public class SummonSkeletonsItem extends KitPvpAbility {
    public SummonSkeletonsItem(Properties pProperties) {
        super(pProperties);
        setCooldown(20);
    }

    @Override
    protected void onUse(ServerLevel level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            var archer = new FriendlySkeleton(ModEntities.FRIENDLY_SKELETON.get(), level, player);
            var wither = new FriendlyWitherSkeleton(ModEntities.FRIENDLY_WITHER_SKELETON.get(), level, player);
            archer.moveTo(player.getX() + 1, player.getY(), player.getZ(), player.getYRot(), 0f);
            wither.moveTo(player.getX() - 1, player.getY(), player.getZ(), player.getYRot(), 0f);
            archer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
            wither.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
            level.addFreshEntity(archer);
            level.addFreshEntity(wither);
        }
        player.swing(hand, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Призывает!"));
        tooltip.add(Component.literal("§8Перезарядка: 20 секунд"));
    }
}


