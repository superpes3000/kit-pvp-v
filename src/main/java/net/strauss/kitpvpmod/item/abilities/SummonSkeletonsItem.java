package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.mob.FriendlySkeleton;
import net.strauss.kitpvpmod.entity.mob.FriendlyWitherSkeleton;
import net.strauss.kitpvpmod.entity.ModEntities;

public class SummonSkeletonsItem extends Item {
    public SummonSkeletonsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()) {
            var archer = new FriendlySkeleton(ModEntities.FRIENDLY_SKELETON.get(), level, player);
            var wither = new FriendlyWitherSkeleton(ModEntities.FRIENDLY_WITHER_SKELETON.get(), level, player);
            archer.moveTo(player.getX() + 1, player.getY(), player.getZ(), player.getYRot(), 0f);
            wither.moveTo(player.getX() - 1, player.getY(), player.getZ(), player.getYRot(), 0f);
            archer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
            wither.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
            level.addFreshEntity(archer);
            level.addFreshEntity(wither);
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
