package net.strauss.kitpvpmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class SwordProjectileEntity extends ThrowableItemProjectile {
    private Vec3 startPos;
    @Override
    protected double getDefaultGravity() {
        return 0.0f;
    }

    // 🔹 Обязательный конструктор, который вызывается при /summon
    public SwordProjectileEntity(EntityType<? extends SwordProjectileEntity> type, Level level) {
        super(type, level);
    }

    // 🔹 Конструктор, который используешь при броске с предмета
    public SwordProjectileEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(ModEntities.SWORD_PROJECTILE.get(), owner, level);
        this.setItem(stack.copy());
        this.startPos = this.position();
    }

    // 🔹 Безопасный дефолтный предмет — нельзя возвращать null
    @Override
    protected Item getDefaultItem() {
        return Items.IRON_SWORD; // можно заменить на свой меч
    }

    // 🔹 Выключаем гравитацию


    // 🔹 Проверяем расстояние и удаляем после 20 блоков
    @Override
    public void tick() {
        super.tick();
        if (startPos == null) startPos = this.position();

        if (this.position().distanceToSqr(startPos) > 20 * 20) {
            this.discard();
            return;
        }
    }

    // 🔹 Обработка попадания
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity target = hitResult.getEntity();
        Entity owner = this.getOwner();

        if (target instanceof LivingEntity living && owner instanceof LivingEntity attacker) {
            DamageSource source = this.damageSources().thrown(this, attacker);
            living.hurt(source, 6.0F); // урон
        }

        this.discard(); // исчезает после попадания
    }
}
