package com.example.mod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RideableBee extends Bee {

    public RideableBee(EntityType<? extends Bee> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        // Проверяем, сидит ли на пчеле игрок
        if (this.getPassengers().size() > 0 && this.getPassengers().get(0) instanceof Player player) {
            // Получаем направление взгляда игрока
            Vec3 look = player.getLookAngle();

            // Двигаем пчелу в этом направлении
            this.setDeltaMovement(look.x * 0.5, look.y * 0.5, look.z * 0.5);
            this.moveRelative(0.1f, new Vec3(look.x, look.y, look.z));

            // Синхронизация поворота
            this.setYRot(player.getYRot());
            this.setXRot(player.getXRot());


        }
    }
}
