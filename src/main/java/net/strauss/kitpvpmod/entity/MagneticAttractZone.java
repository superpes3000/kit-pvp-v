package net.strauss.kitpvpmod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class MagneticAttractZone extends Entity {

    private static final double RADIUS = 8;
    private static final double STRENGTH = 0.15;
    private int ticks = 0;
    private Level level;
    private Vec3 center;

    public MagneticAttractZone(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

    }

    public MagneticAttractZone(Level level, Vec3 center) {
        super(EntityType.AREA_EFFECT_CLOUD, level); // Можно использовать AreaEffectCloud
        this.center = center;
        this.level = level;
        this.noPhysics = true; // чтобы не падала
        this.setPos(center);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) return;

        ticks++;
        if (ticks > 600) { // зона живет 30 секунд
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        // Притягиваем все сущности в радиусе
        AABB area = new AABB(center.x - RADIUS, center.y - RADIUS, center.z - RADIUS,
                center.x + RADIUS, center.y + RADIUS, center.z + RADIUS);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity e : entities) {
            Vec3 dir = center.subtract(e.position());
            double distance = dir.length();
            if (distance > 0.1) {
                e.setDeltaMovement(e.getDeltaMovement().add(dir.normalize().scale(STRENGTH)));
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
