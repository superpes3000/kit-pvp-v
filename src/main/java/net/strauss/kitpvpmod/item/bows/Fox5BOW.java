package net.strauss.kitpvpmod.item.bows;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class Fox5BOW extends BowItem {

    public Fox5BOW(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft)
    {
        pEntityLiving.heal(5);

        if (pEntityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(pStack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(pStack, pEntityLiving) - pTimeLeft;
                i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, true);
                if (i < 0) return;

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(pStack, itemstack, player);
                    if (pLevel instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), pStack, list, f * 3.0F, 1.0F, f == 1.0F, null);
                    }

                    pLevel.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.SHEEP_HURT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
        if (!pLevel.isClientSide && pEntityLiving instanceof ServerPlayer player) {
            if(new Random().nextInt(0, 100) > 50){
                player.sendSystemMessage(Component.literal("Ты пидр"));

            }
            else {
                new Thread(() -> {
                    try {
                        URL url = new URL("https://icanhazdadjoke.com/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestProperty("Accept", "text/plain");
                        conn.setRequestProperty("User-Agent", "Minecraft Forge Mod");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String joke = reader.readLine();
                        reader.close();

                        // Отправляем шутку игроку
                        player.sendSystemMessage(Component.literal("Анекдот: " + joke));

                    } catch (Exception e) {
                        player.sendSystemMessage(Component.literal("Не удалось получить анекдот :("));
                        e.printStackTrace();
                    }
                }).start();
            }


        }

    }
}
