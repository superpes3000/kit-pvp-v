package com.example.mod.item;

import com.example.mod.entity.RideableBee;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.strauss.kitpvpmod.entity.ModEntities;
import net.strauss.kitpvpmod.entity.mob.BearAggressiveBee;
import net.strauss.kitpvpmod.item.abilities.KitPvpAbility;

import java.util.List;

public class BearBeeMount extends KitPvpAbility {
    public BearBeeMount(Properties properties) {
        super(properties);
        setCooldown(15);
    }
    @Override
    protected void onUse(ServerLevel serverLevel, Player player, InteractionHand hand) {
        Vec3 spawnPos = player.position().add(player.getLookAngle().multiply(1, 0, 1));
        Bee bee = new RideableBee(EntityType.BEE, serverLevel);
        bee.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), player.getXRot());
        serverLevel.addFreshEntity(bee);

        // Сажаем игрока на пчелу
        player.startRiding(bee, true);

        // Таймер на 5 секунд (100 тиков)
        serverLevel.getServer().execute(() -> removeBeeAfterDelay(serverLevel, bee, player));

        var aggressiveBee = new BearAggressiveBee(ModEntities.BEAR_AGGRESSIVE_BEE.get(), serverLevel, player);
        var aggressiveBee2 = new BearAggressiveBee(ModEntities.BEAR_AGGRESSIVE_BEE.get(), serverLevel, player);

        aggressiveBee.moveTo(player.getX() - 1, player.getY(), player.getZ(), player.getYRot(), 0f);
        aggressiveBee2.moveTo(player.getX() + 1, player.getY(), player.getZ(), player.getYRot(), 0f);


        serverLevel.addFreshEntity(aggressiveBee);
        serverLevel.addFreshEntity(aggressiveBee2);

        player.swing(hand, true);

    }


    private void removeBeeAfterDelay(Level world, Bee bee, Player player) {

        new Thread(() -> {
            try {
                Thread.sleep(5000); // 5 секунд
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!world.isClientSide && bee.isAlive()) {

                player.stopRiding();
                bee.remove(Entity.RemovalReason.DISCARDED);
            }
        }).start();
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Вы летите на пчеле 5 секунд."));
        tooltip.add(Component.literal("§7Спавнит 2 агрессивные пчелы."));

    }
}
