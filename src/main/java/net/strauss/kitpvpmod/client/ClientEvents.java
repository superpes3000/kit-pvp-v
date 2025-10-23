package net.strauss.kitpvpmod.client;

import net.strauss.kitpvpmod.KitPvpMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    // ⏳ Список кулдаунов для каждого игрока
    private static final HashMap<UUID, Long> dashCooldowns = new HashMap<>();

    // Время кулдауна в миллисекундах
    private static final long COOLDOWN_MS = 2000; // 2 секунды

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        Player player = mc.player;
        UUID id = player.getUUID();

        // Проверяем, нажата ли клавиша Z
        if (Keybinds.DASH_KEY.consumeClick()) {
            long currentTime = System.currentTimeMillis();
            long lastDash = dashCooldowns.getOrDefault(id, 0L);

            // Проверка кулдауна
            if (currentTime - lastDash < COOLDOWN_MS) {
                long left = (COOLDOWN_MS - (currentTime - lastDash)) / 1000;
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("⏳ Перезарядка: " + left + " сек."),
                        true
                );
                return;
            }

            // ✅ Делаем рывок
            Vec3 look = player.getLookAngle();
            double dashStrength = 2.0;

            Vec3 dash = new Vec3(look.x * dashStrength, 0.1, look.z * dashStrength);
            player.setDeltaMovement(dash);
            player.hasImpulse = true;

            // Сохраняем время последнего рывка
            dashCooldowns.put(id, currentTime);

            // 🎵 Звук и частицы
            mc.level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT,
                    net.minecraft.sounds.SoundSource.PLAYERS,
                    1.0F, 1.0F, false);

            for (int i = 0; i < 20; i++) {
                mc.level.addParticle(net.minecraft.core.particles.ParticleTypes.PORTAL,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        (Math.random() - 0.5) * 1.5, 0.1, (Math.random() - 0.5) * 1.5);
            }

            // Сообщение
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Рывок вперёд!"),
                    true
            );
        }
    }
}