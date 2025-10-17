package net.kaupenjoe.tutorialmod.item.swords;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class Fox5Sword extends SwordItem {

    public Fox5Sword(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker)
    {
        pAttacker.heal(5);

        return true;
    }

}
