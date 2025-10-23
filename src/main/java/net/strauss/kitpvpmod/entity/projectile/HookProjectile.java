package net.strauss.kitpvpmod.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HookProjectile extends Projectile {

    private LivingEntity owner;
    private LivingEntity hookedTarget;
    private boolean pulling = false;

    public HookProjectile(EntityType<? extends HookProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public HookProjectile(EntityType<? extends HookProjectile> entityType, Level level, LivingEntity owner) {
        super(entityType, level);
        this.owner = owner;
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY(), owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            // Если уже зацепили цель — начинаем притягивание
            if (pulling && hookedTarget != null && owner != null) {
                pullTargetToPlayer();
            } else {
                // Иначе продолжаем полёт и проверяем столкновение
                HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    this.onHitEntity((EntityHitResult) hitResult);
                }
                this.move();
            }
        }
    }

    private void move() {
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target) {
            this.hookedTarget = target;
            this.pulling = true;
        }
    }

    private void pullTargetToPlayer() {
        Vec3 playerPos = owner.position().add(0, 1, 0); // тянем к уровню глаз
        Vec3 targetPos = hookedTarget.position();
        Vec3 direction = playerPos.subtract(targetPos).normalize().scale(0.5); // скорость 0.5
        hookedTarget.setDeltaMovement(direction);

        // Если цель близко — удаляем крюк
        if (targetPos.distanceTo(playerPos) < 1.5) {
            this.discard();
        }
    }
}
