package net.strauss.kitpvpmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.strauss.kitpvpmod.entity.ModEntities;
import net.strauss.kitpvpmod.entity.projectile.HookProjectile;

public class HookItem extends Item {

    public HookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            HookProjectile hook = new HookProjectile(ModEntities.HOOK_PROJECTILE.get(), level, player);
            hook.setOwner(player);
            hook.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
            level.addFreshEntity(hook);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
