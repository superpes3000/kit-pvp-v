package net.kaupenjoe.tutorialmod.entity.custom;

import net.kaupenjoe.tutorialmod.entity.GrenadeEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CrowProjectile extends ThrowableItemProjectile {

    public CrowProjectile(EntityType<? extends GrenadeEntity> type, Level level) {
        super(type, level);
    }

    public CrowProjectile(Level level, LivingEntity shooter) {
        super(EntityType.SNOWBALL, shooter, level); // Важно: здесь позже можно заменить на свой EntityType
    }

    public CrowProjectile(Level level, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            // Наносим 1 урона (1 сердце)

            result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 2.0F);
        }
    }
}
