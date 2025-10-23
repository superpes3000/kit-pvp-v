package net.strauss.kitpvpmod.events;

import net.strauss.kitpvpmod.KitPvpMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID)
public class HungerHandler {
    private static int tickCounter = 0;
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.getCommandSenderWorld().isClientSide) {
            int foodLevel = event.player.getFoodData().getFoodLevel();
            float saturation = event.player.getFoodData().getSaturationLevel();

            event.player.getFoodData().setFoodLevel(20);
            event.player.getFoodData().setSaturation(0f); // убираем насыщение
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getFoodData().getFoodLevel() == 0) {
                tickCounter++;
                // Раз в 20 тиков (1 секунда)
                if (tickCounter % 10 == 0) {
                    event.setAmount(event.getAmount() * 0.1F); // очень медленная реген
                } else {
                    event.setAmount(0F); // остальные тики отменяем
                }
            }
        }
    }
}
