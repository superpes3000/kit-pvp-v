package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.BearHoneySnowballEntity;
import net.strauss.kitpvpmod.entity.MagneticAttractSnowballEntity;
import net.strauss.kitpvpmod.entity.mob.BearHoneyEntity;

import java.util.List;

public class BearHoneyItem extends Item implements ProjectileItem {
    public BearHoneyItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.getCooldowns().isOnCooldown(this)) {
            ItemStack stack = pPlayer.getItemInHand(pHand);
            return InteractionResultHolder.fail(stack);
        }

        pPlayer.getCooldowns().addCooldown(this, 10 * 20);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound(
                null,
                pPlayer.getX(),
                pPlayer.getY(),
                pPlayer.getZ(),
                SoundEvents.ENDER_PEARL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!pLevel.isClientSide) {
            BearHoneySnowballEntity snowball = new BearHoneySnowballEntity(pLevel, pPlayer);
            snowball.setItem(itemstack);
            snowball.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(snowball);
        }


        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level pLevel, Position pPos, ItemStack pStack, Direction pDirection) {
        Snowball snowball = new Snowball(pLevel, pPos.x(), pPos.y(), pPos.z());
        snowball.setItem(pStack);
        return snowball;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Вы кидаете лужу мёда в котором все кроме вас получают замедление."));
        tooltip.add(Component.literal("§7Спавнит агрессивную пчелу."));

    }
}