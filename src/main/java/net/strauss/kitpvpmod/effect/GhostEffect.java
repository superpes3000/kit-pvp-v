package com.example.yourmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GhostEffect extends MobEffect {
    public GhostEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x99ccff); // Категория и цвет (для UI)
    }
    @Override
    public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide) {
            pLivingEntity.setInvisible(true);
        }
    }
    @Override
    public void onMobRemoved(LivingEntity pLivingEntity, int pAmplifier, Entity.RemovalReason pReason) {
        pLivingEntity.setInvisible(false); // вернуть видимость, когда эффект заканчивается
    }

}
