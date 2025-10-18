package net.kaupenjoe.tutorialmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class FriendlySkeleton extends Skeleton {
    private UUID ownerUUID;
    private int lifeTicks = 200; // 10 секунд (20 тиков * 10)

    public FriendlySkeleton(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    public void setOwner(UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        // Не атаковать владельца
        if (target instanceof Player player && ownerUUID != null && player.getUUID().equals(ownerUUID)) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (--lifeTicks <= 0) {
                this.discard(); // Удаляем через 10 секунд
            }
        }
    }
}
