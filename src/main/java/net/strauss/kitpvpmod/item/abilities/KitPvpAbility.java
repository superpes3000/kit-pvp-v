package net.strauss.kitpvpmod.item.abilities;

import net.strauss.kitpvpmod.item.MinecraftColor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class KitPvpAbility extends Item {
    protected int Cooldown = 0;
    private boolean isCooldownSet;
    public KitPvpAbility(Properties pProperties) {
        super(pProperties);
    }
    protected void setCooldown(int cooldown){
        isCooldownSet = true;
        Cooldown = cooldown;
    }
    protected abstract void onUse(ServerLevel serverLevel, Player player, InteractionHand hand);

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }
            if(isCooldownSet){
                player.getCooldowns().addCooldown(this, Cooldown * 20);
            }
            onUse((ServerLevel) world, player, hand);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }
    protected void applyDescription(MinecraftColor color){

    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Делает рывок вперед"));
    }
}
