package net.strauss.kitpvpmod.entity.mob;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RideableSlimeEntity extends Slime {

    public RideableSlimeEntity(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.setSize(1, true); // чуть больше стандартного
    }

    public RideableSlimeEntity(Level level) {
        this(EntityType.SLIME, level);
    }

    private int jumpCooldown = 0;
    private Vec3 lastDirection = null;
    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && this.getPassengers().size() > 0 && this.getPassengers().get(0) instanceof Player player) {



            // сохраняем последнее направление, куда нажал игрок
            if (player.zza != 0 || player.xxa != 0) {
                // направление по взгляду игрока + кнопки
                Vec3 dir = new Vec3(player.xxa, 0, player.zza);
                dir = dir.yRot(-player.getYRot() * ((float)Math.PI / 180F)).normalize();

                // сохраняем это направление как "последнее"
                this.lastDirection = dir;
            }

            // если есть сохранённое направление — двигаемся в нём
            if (this.lastDirection != null) {
                double speed = 0.23; // скорость движения
                Vec3 motion = this.lastDirection.scale(speed);
                this.setDeltaMovement(motion.x, this.getDeltaMovement().y, motion.z);
                this.move(MoverType.SELF, getDeltaMovement());
            }

            // Поворачиваем пчелу лицом туда, куда смотрит игрок
            this.setYRot(player.getYRot());
            this.yBodyRot = player.getYRot();
            this.yHeadRot = player.getYRot();
        }
    }

    /*@Override
    public void travel(Vec3 travelVector) {
        Entity rider = getControllingPassenger();

        if (rider instanceof Player player) {
            // вращаем слизня в ту же сторону, куда смотрит игрок
            setYRot(player.getYRot());
            setXRot(player.getXRot() * 0.5f);
            this.yBodyRot = player.getYRot();
            this.yHeadRot = player.getYRot();

            // извлекаем управление игрока
            float forward = player.zza; // W/S
            float strafe = player.xxa;  // A/D

            // базовая скорость
            double speed = this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 1.5;

            // движение вперёд/назад/в стороны
            Vec3 motion = new Vec3(strafe * 0.4, 0, forward).yRot(-this.getYRot() * ((float) Math.PI / 180F));
            if (forward != 0 || strafe != 0) {
                setDeltaMovement(motion.normalize().scale(speed));
            } else {
                setDeltaMovement(Vec3.ZERO);
            }

            move(MoverType.SELF, getDeltaMovement());
            return;
        }

        // если никто не управляет — обычное поведение
        super.travel(travelVector);
    }
    */


    @Override
    public void travel(Vec3 travelVector) {
        // Отключаем стандартное управление
        if (isVehicle() && getControllingPassenger() instanceof Player) {
            move(MoverType.SELF, getDeltaMovement());
            return;
        }
        super.travel(travelVector);
    }




    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {
        // без AI
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }


    @Override
    protected void tickDeath() {
        super.tickDeath();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
    }

    @Override
    public void ejectPassengers() {
        super.ejectPassengers();
        if (!level().isClientSide) {
            discard(); // слизень умирает при спешивании
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }
}
