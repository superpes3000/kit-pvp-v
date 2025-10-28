package net.strauss.kitpvpmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.strauss.kitpvpmod.entity.mob.BearAggressiveBee;
import net.strauss.kitpvpmod.entity.mob.BearHoneyEntity;

public class BearHoneySnowballEntity extends ThrowableItemProjectile {

    private Player owner;
    public BearHoneySnowballEntity(EntityType<? extends BearHoneySnowballEntity> type, Level level) {
        super(type, level);
    }

    public BearHoneySnowballEntity(Level level, LivingEntity shooter) {
        super(EntityType.SNOWBALL, shooter, level); // Важно: здесь позже можно заменить на свой EntityType
    }
    public BearHoneySnowballEntity(Level level, Player shooter) {
        super(EntityType.SNOWBALL, shooter, level); // Важно: здесь позже можно заменить на свой EntityType
        owner = shooter;
    }

    public BearHoneySnowballEntity(Level level, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!level().isClientSide) {
            var pos =             result.getBlockPos();



            var aggressiveBee = new BearAggressiveBee(ModEntities.BEAR_AGGRESSIVE_BEE.get(), level(), owner);

            aggressiveBee.moveTo(pos.getX() - 1, pos.getY() + 1, pos.getZ(), 0, 0f);


            level().addFreshEntity(aggressiveBee);

            BlockPos pos2 = result.getBlockPos().above();
            BearHoneyEntity trap = new BearHoneyEntity(level(), pos2, owner);
            level().addFreshEntity(trap);
            this.discard();
        }
    }
}
