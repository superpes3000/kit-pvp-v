package net.strauss.kitpvpmod.item.abilities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.strauss.kitpvpmod.utils.EffectGiver;

import java.util.List;

public class GhostInvisibility extends KitPvpAbility {

    public GhostInvisibility(Properties properties) {
        super(properties);
        setCooldown(30);
    }

    @Override
    protected void onUse(ServerLevel serverLevel, Player player, InteractionHand hand) {

        EffectGiver.applyMobEffect(player, MobEffects.INVISIBILITY, 8, 0);

        player.swing(hand, true);

    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return; // выполняем только в конце тика
        if (!(event.player instanceof ServerPlayer player)) return; // только сервер


        boolean hasTag = player.isInvisible();

        if (hasTag) {
            EffectGiver.applyMobEffect(player, MobEffects.MOVEMENT_SPEED, 8, 0);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Добавляем описание
        tooltip.add(Component.literal("§7Даёт невидимость и скорость на 8 секунд."));
        tooltip.add(Component.literal("§7При ударе, при броске меча невидимость снимается"));
        tooltip.add(Component.literal("§8Перезарядка: 30 секунд"));

    }
}
