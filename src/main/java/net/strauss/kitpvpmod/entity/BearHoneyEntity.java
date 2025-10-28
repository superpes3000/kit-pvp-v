package net.strauss.kitpvpmod.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.strauss.kitpvpmod.entity.ModEntities;
import org.joml.Vector3f;

public class BearHoneyEntity extends Entity {

    private Player owner;
    private int lifetime = 100; // живёт 5 секунд
    private final double radius = 5.0; // радиус облака
    public BearHoneyEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

    }

    public BearHoneyEntity(Level level, BlockPos pos, Player owner) {
        this(ModEntities.BEAR_HONEY_ENTITY.get(), level);
        this.owner = owner;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();
        float r = 1.0f;   // красный
        float g = 1.0f;   // зелёный
        float b = 0.3f;   // синий → желтый
        float scale = 1.0f; // размер частиц
        if (level().isClientSide) {
            // Спавним желтые частицы для визуализации облака
            for (int i = 0; i < 25; i++) { // больше частиц для круга
                double angle = random.nextDouble() * Math.PI * 2; // угол
                double rad = random.nextDouble() * radius; // случайное расстояние до центра
                double offsetX = Math.cos(angle) * rad;
                double offsetZ = Math.sin(angle) * rad;
                double offsetY = random.nextDouble() * 1.0; // чуть выше земли
                level().addParticle(new DustParticleOptions(new Vector3f(r, g, b), scale),
                        getX() + offsetX, getY() + offsetY, getZ() + offsetZ,
                        0, 0, 0);
            }
            return;
        }

        // Серверная логика: ищем сущностей в квадрате и фильтруем по кругу
        AABB aabb = new AABB(getX() - radius, getY(), getZ() - radius,
                getX() + radius, getY() + 1.5, getZ() + radius);
        for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, aabb)) {
            double dx = entity.getX() - getX();
            double dz = entity.getZ() - getZ();
            if (  dx * dx + dz * dz <= radius * radius && entity != owner ) { // проверка попадания в круг
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3, false, false));
            }
        }

        // Срок жизни
        if (--lifetime <= 0) {
            this.discard();
        }
    }


    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}
