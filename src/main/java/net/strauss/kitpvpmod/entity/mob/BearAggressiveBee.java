package net.strauss.kitpvpmod.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BearAggressiveBee extends Bee implements FriendlyMob {

    private static final String OWNER_TAG = "OwnerUUID";
    @Nullable
    private UUID ownerUuid;
    public Player Owner;
    @Override
    public boolean hasStung() {
        return false;
    }


    @Override
    public boolean doHurtTarget(Entity target) {
        DamageSource damagesource = this.damageSources().sting(this);
        boolean flag = target.hurt(damagesource, (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            if (this.level() instanceof ServerLevel serverlevel) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, target, damagesource);
            }


            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }

        this.setHealth(this.getMaxHealth());
        return flag;
    }

    public BearAggressiveBee(EntityType<? extends Bee> type, Level world) {
        super(type, world);
    }

    public BearAggressiveBee(EntityType<? extends Bee> type, Level world, Player owner) {
        super(type, world);
        this.ownerUuid = owner.getUUID();
        Owner = owner;
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
            return false;
        }
        // Пчела больше не считает владельца союзником — он становится целью
        return super.isAlliedTo(other);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Цель — атаковать только владельца
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this,
                Player.class,
                10,
                true,
                true,
                target -> this.ownerUuid != null && target != null && isOwner(target) // только владелец
        ));
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
    public @Nullable UUID getOwner() {
        return this.ownerUuid;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4) // можно увеличить здоровье
                .add(Attributes.FLYING_SPEED, 1.8f)
                .add(Attributes.FOLLOW_RANGE, 50)
                .add(Attributes.ATTACK_DAMAGE, 2.5); // урон по владельцу
    }
}
