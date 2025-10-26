package net.strauss.kitpvpmod.events;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber
public class GhostInvisibilityEvent {

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();

        // Если игрок имеет тег невидимости
        if (player.isInvisible()) {
            player.setInvisible(false);
        }
    }
}
