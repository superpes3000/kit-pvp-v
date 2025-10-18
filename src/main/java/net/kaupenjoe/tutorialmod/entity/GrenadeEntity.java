package net.kaupenjoe.tutorialmod.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrenadeEntity extends ThrowableItemProjectile {

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, Level level) {
        super(type, level);
    }

    public GrenadeEntity(Level level, LivingEntity shooter) {
        super(EntityType.SNOWBALL, shooter, level); // Важно: здесь позже можно заменить на свой EntityType
    }

    public GrenadeEntity(Level level, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        // создаем визуальные частицы взрыва

        for (int i = 0; i < 20; i++) {
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(),
                    (Math.random() - 0.5) * 0.5, Math.random() * 0.5, (Math.random() - 0.5) * 0.5);
        }
        if (!this.level().isClientSide) {
            // создаем визуальные частицы взрыва
            for (int i = 0; i < 20; i++) {
                this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(),
                        (Math.random() - 0.5) * 0.5, Math.random() * 0.5, (Math.random() - 0.5) * 0.5);
            }

            // Радиус действия гранаты
            double radius = 5.0;
            AABB area = new AABB(this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                    this.getX() + radius, this.getY() + radius, this.getZ() + radius);

            // Все игроки в радиусе получают слепоту
            for (Player player : this.level().getEntitiesOfClass(Player.class, area)) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0)); // 5 секунд
            }

            // Удаляем гранату после взрыва
            this.discard();
        }
    }
}