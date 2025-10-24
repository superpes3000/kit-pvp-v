package net.strauss.kitpvpmod.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HookProjectile extends Projectile {

    private static final EntityDataAccessor<Boolean> PULLING = SynchedEntityData.defineId(HookProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HOOKED_TARGET_ID = SynchedEntityData.defineId(HookProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(HookProjectile.class, EntityDataSerializers.INT);

    private LivingEntity owner; // на сервере установится в конструкторе, на клиенте читается из entityData
    private LivingEntity hookedTarget;
    private boolean pulling = false;

    private static final double MAX_FLIGHT_DISTANCE = 40.0;
    private static final float PARTICLE_SPACING = 1f;

    public HookProjectile(EntityType<? extends HookProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public HookProjectile(EntityType<? extends HookProjectile> entityType, Level level, LivingEntity owner) {
        super(entityType, level);
        this.owner = owner;
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY(), owner.getZ());

        if (!level.isClientSide) {
            this.entityData.set(OWNER_ID, owner.getId());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(PULLING, false);
        pBuilder.define(HOOKED_TARGET_ID, -1);
        pBuilder.define(OWNER_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.owner == null) {
            int ownerId = this.entityData.get(OWNER_ID);
            if (ownerId != -1) {
                if (level().getEntity(ownerId) instanceof LivingEntity le) {
                    this.owner = le;
                }
            }
        }

        if (!level().isClientSide) {
            if (!this.pulling && this.owner != null) {
                double dist = this.position().distanceTo(this.owner.position());
                if (dist > MAX_FLIGHT_DISTANCE) {
                    this.discard();
                    return;
                }
            }

            if (this.pulling && this.hookedTarget != null && this.owner != null) {
                pullTargetToPlayer();
                spawnPullingParticlesServer(ParticleTypes.ENCHANT);
                return;
            } else {
                spawnPullingParticlesServer(ParticleTypes.CRIT);
            }

            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                this.onHitEntity((EntityHitResult) hitResult);
            } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                this.onHitBlock((BlockHitResult) hitResult);
            } else {
                this.move();
            }

        } else {
            int ownerId = this.entityData.get(OWNER_ID);
            if (ownerId != -1 && (this.owner == null || this.owner.getId() != ownerId)) {
                if (level().getEntity(ownerId) instanceof LivingEntity le) {
                    this.owner = le;
                }
            }

            int targetId = this.entityData.get(HOOKED_TARGET_ID);
            if (targetId != -1) {
                if (this.hookedTarget == null || this.hookedTarget.getId() != targetId) {
                    if (level().getEntity(targetId) instanceof LivingEntity t) {
                        this.hookedTarget = t;
                    }
                }
            } else {
                this.hookedTarget = null;
            }

            this.pulling = this.entityData.get(PULLING);
        }
    }

    private void spawnPullingParticlesServer(SimpleParticleType type) {
        Level level = level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (owner == null) return;

        Vec3 start = owner.getEyePosition();
        start = start.add(0, -0.2, 0);

        Vec3 end;
        if (hookedTarget != null) {
            end = hookedTarget.position().add(0, hookedTarget.getEyeHeight() * 0.3f, 0);
        } else {
            end = this.position();
        }

        Vec3 diff = end.subtract(start);
        double length = diff.length();
        if (length <= 0.01) return;

        Vec3 dir = diff.normalize();
        int count = Math.max(2, (int) (length / PARTICLE_SPACING));

        for (int i = 0; i <= count; i++) {
            double t = (double) i / (double) count;
            Vec3 pos = start.add(dir.scale(length * t));

            serverLevel.sendParticles(type,
                    pos.x, pos.y, pos.z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0);
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
            this.entityData.set(PULLING, true);
            this.entityData.set(HOOKED_TARGET_ID, target.getId());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.discard();
    }

    private void pullTargetToPlayer() {
        if (owner == null || hookedTarget == null) return;

        Vec3 playerPos = owner.position().add(0, 1, 0);
        Vec3 targetPos = hookedTarget.position();
        Vec3 dir = playerPos.subtract(targetPos);
        if (dir.lengthSqr() < 0.001) return;

        Vec3 vel = dir.normalize().scale(0.75);
        hookedTarget.setDeltaMovement(vel);

        if (targetPos.distanceTo(playerPos) < 1.5) {
            this.discard();
        }
    }
}
