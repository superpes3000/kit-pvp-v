package net.strauss.kitpvpmod.events;

import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@Mod.EventBusSubscriber
public class ArrowTeleportHandler {

    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent event) {
        // Проверяем, что снаряд — это стрела
        /*
        if (event.getProjectile() instanceof Arrow arrow) {
            var player = arrow.getOwner();
            //if (arrow.getOwner() instanceof ServerPlayer player) {
                HitResult hit = event.getRayTraceResult();
                Vec3 hitPos = hit.getLocation(); // Координаты попадания

                // Телепортируем игрока на место попадания стрелы
                //player.teleportTo(hitPos.x, hitPos.y, hitPos.z);

                // Отправляем сообщение
                player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal("Телепортация на место стрелы!")
                );
            //}
        }
        */

    }
}
