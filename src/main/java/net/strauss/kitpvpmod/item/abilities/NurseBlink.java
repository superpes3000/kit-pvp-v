package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NurseBlink extends Item {

    public NurseBlink(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

            if (player.getCooldowns().isOnCooldown(this)) {
                ItemStack stack = player.getItemInHand(hand);
                return InteractionResultHolder.fail(stack);
            }

            int dashStrength = 1;
            player.getCooldowns().addCooldown(this, 30);

            Vec3 look = player.getLookAngle();
            Vec3 dash = look.scale(dashStrength);

            Vec3 start = player.position();
            Vec3 end = start.add(look.scale(dashStrength * 6)); // расстояние дэша

            player.setDeltaMovement(dash);
            player.hurtMarked = true;

            ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
            BlockHitResult hit = player.level().clip(context);

            if (hit.getType() == HitResult.Type.MISS) {
                player.teleportTo(end.x, end.y, end.z);
            } else {
                BlockPos pos = hit.getBlockPos();
                BlockState state = player.level().getBlockState(pos);
                if (!state.is(Blocks.BARRIER)) {
                    // можно пройти сквозь блок, если это не барьер
                    player.teleportTo(end.x, end.y, end.z);
                } else {
                    // если барьер — останавливаемся перед ним
                    Vec3 stop = Vec3.atCenterOf(pos).subtract(look.scale(1.0));
                    player.teleportTo(stop.x, stop.y, stop.z);
                }
            }

            // проверка на барьер
            Vec3 nextPos = player.position().add(dash);
            BlockState nextBlock = player.level().getBlockState(BlockPos.containing(nextPos));
            if (nextBlock.is(Blocks.BARRIER)) {
                // останавливаем игрока прямо перед барьером
                Vec3 stop = player.position().subtract(look.scale(0.5));
                player.teleportTo(stop.x, stop.y, stop.z);
                player.setDeltaMovement(Vec3.ZERO);
            }

            // вернуть физику обратно через несколько тиков


        }

        player.swing(hand, true);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Делает рывок вперед"));
    }

}
