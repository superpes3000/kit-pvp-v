package net.strauss.kitpvpmod.item.abilities;

import net.strauss.kitpvpmod.entity.custom.TomahawkProjectileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TomahawkEscape extends Item {
    public TomahawkEscape(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {


        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemstack);
        }

        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            TomahawkProjectileEntity tomahawkProjectile = new TomahawkProjectileEntity(pPlayer, pLevel);
            tomahawkProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 0F);
            pLevel.addFreshEntity(tomahawkProjectile);

            Vec3 look = pPlayer.getLookAngle();
            double dashStrength = 2.0; // сила рывка


            Vec3 dash = new Vec3(look.x * -dashStrength, 0.5f, look.z * -dashStrength);

            pPlayer.setDeltaMovement(dash);
            pPlayer.hurtMarked = true; // чтобы сервер отправил новое положение клиенту
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        pPlayer.getCooldowns().addCooldown(this, 200);


        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Герой бросает топор и отпрыгивает назад"));
        tooltip.add(Component.literal("§8Перезарядка: 10 секунд"));
    }

}