package net.strauss.kitpvpmod.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FriendlySkeleton extends Skeleton implements FriendlyMob {
    private static final String OWNER_TAG = "OwnerUUID";
    @Nullable
    private UUID ownerUuid;


    public FriendlySkeleton(EntityType<? extends Skeleton> type, Level world) {
        super(type, world);
    }
    public FriendlySkeleton(EntityType<? extends Skeleton> type, Level world, LivingEntity owner) {
        super(type, world);
        this.ownerUuid = owner.getUUID();
    }
    @Nullable
    public UUID getOwner() {
        return this.ownerUuid;
    }

    public boolean isOwner(LivingEntity e) {
        if (ownerUuid == null || e == null) return false;
        if (e instanceof Player) {
            return ownerUuid.equals(e.getUUID());
        }
        return false;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity other) {
        if (other instanceof Player && isOwner((Player) other)) {
            return true;
        }
        if (other instanceof FriendlyMob skeleton) {
            return Objects.equals(skeleton.getOwner(), this.getOwner());
        }
        if (other instanceof Player && ownerUuid != null) {
            UUID otherUuid = other.getUUID();
            if (ownerUuid.equals(otherUuid)) return true;
        }
        return super.isAlliedTo(other);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        Predicate<LivingEntity> excludeOwner = (LivingEntity target) -> {
            if (target == null) return false;
            if (isOwner(target)) return false;
            return true;
        };
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, excludeOwner));
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUuid != null) {
            tag.putUUID(OWNER_TAG, this.ownerUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID(OWNER_TAG)) {
            this.ownerUuid = tag.getUUID(OWNER_TAG);
        } else {
            this.ownerUuid = null;
        }
    }

    @Override
    public boolean isSunBurnTick() {
        return false;
    }
}
