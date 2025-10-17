package net.kaupenjoe.tutorialmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;

public class FlashbangEntity extends ThrowableProjectile {

    public FlashbangEntity(EntityType<? extends FlashbangEntity> type, Level level) {
        super(type, level);
    }

    public FlashbangEntity(EntityType<? extends FlashbangEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.HitResult result) {
        explode();
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitBlock(net.minecraft.world.phys.HitResult result) {
        explode();
        this.remove(RemovalReason.DISCARDED);
    }

    private void explode() {
        // Радиус действия
        double radius = 5.0;

        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(
                getX() - radius, getY() - radius, getZ() - radius,
                getX() + radius, getY() + radius, getZ() + radius
        ));

        for (Player player : players) {
            // Проверяем, смотрит ли игрок на гранату
            if (isLookingAt(player)) {
                if (!player.level.isClientSide) {
                    ((ServerPlayer) player).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1)); // 3 сек
                }
            }
        }
    }

    // Проверка направления взгляда
    private boolean isLookingAt(Player player) {
        // Вектор взгляда
        var look = player.getLookAngle().normalize();
        // Вектор до гранаты
        var dir = new net.minecraft.world.phys.Vec3(getX() - player.getX(), getY() - player.getEyeY(), getZ() - player.getZ()).normalize();
        // Косинус угла между векторами
        double dot = look.dot(dir);

        return dot > 0.85; // примерно ±30° конус обзора
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void onHit(net.minecraft.world.phys.HitResult result) {}
}