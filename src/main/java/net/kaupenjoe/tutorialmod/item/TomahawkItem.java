package net.kaupenjoe.tutorialmod.item.custom;

import com.google.common.collect.Multimap;
import net.kaupenjoe.tutorialmod.entity.custom.TomahawkProjectileEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TomahawkItem extends Item {
    public TomahawkItem(Properties pProperties) {
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
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        pPlayer.getCooldowns().addCooldown(this, 60);

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Можно бросить топор на ПКМ"));
    }


}