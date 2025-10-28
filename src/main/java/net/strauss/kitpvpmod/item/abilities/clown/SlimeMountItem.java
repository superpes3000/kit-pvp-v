package net.strauss.kitpvpmod.item.abilities.clown;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.strauss.kitpvpmod.entity.mob.RideableSlimeEntity;

public class SlimeMountItem extends Item {
    public SlimeMountItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(stack);
            }

            player.getCooldowns().addCooldown(this, 10 * 20);


            RideableSlimeEntity slime = new RideableSlimeEntity(level);
            Vec3 spawnPos = player.position().add(0, 0.1, 0);
            slime.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), 0);
            level.addFreshEntity(slime);

            player.startRiding(slime, true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
