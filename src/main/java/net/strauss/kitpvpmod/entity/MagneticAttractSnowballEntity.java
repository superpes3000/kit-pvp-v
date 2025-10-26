package net.strauss.kitpvpmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MagneticAttractSnowballEntity extends Snowball {
    private Level level;
    public MagneticAttractSnowballEntity(EntityType<? extends Snowball> type, Level level) {
        super(type, level);
    }

    public MagneticAttractSnowballEntity(Level level, net.minecraft.world.entity.LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        createMagnetZone();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        createMagnetZone();
    }

    private void createMagnetZone() {
        if (!level.isClientSide) {
            Vec3 pos = this.position();
            MagneticAttractZone zone = new MagneticAttractZone(level, pos);
            level.addFreshEntity(zone);
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
