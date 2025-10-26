package net.strauss.kitpvpmod.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.dto.PlayerPosition;
import net.strauss.kitpvpmod.item.ModItems;
import net.strauss.kitpvpmod.map.KitPvpMapRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID)
public class SignClickHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();

        // Только серверная сторона
        if (level.isClientSide()) return;

        // Проверяем, есть ли табличка

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SignBlockEntity sign)) return;

        // Получаем NBT таблички
        String classString = "Class";
        String mapString = "Map";
        HolderLookup.Provider provider = level.registryAccess();
        CompoundTag tag = sign.saveWithoutMetadata(provider);
        //CompoundTag tag2 = sign.saveWithoutMetadata(provider);


        String mapTag = tag.getCompound("components").getCompound("minecraft:custom_data").get(mapString).toString().replace("\"", "");

        //String classTag = tag2.getCompound("components").getCompound("minecraft:custom_data").get(classString).toString().replace("\"", "");
        player.sendSystemMessage(Component.literal(tag.getCompound("components").getCompound("minecraft:custom_data").get(mapString).toString().replace("\"", "")));
        player.sendSystemMessage(Component.literal(mapTag));


        if (mapTag != null) {

            var registry = new KitPvpMapRegistry();
            var players = event.getLevel().players();
            List<Player> playersList = new ArrayList<>(players);
            List<PlayerPosition> playerPositions = switch (mapTag) {
                case "random" -> registry.getRandomMap(playersList);
                case "farm" -> registry.getMapByTag("farm", playersList);
                case "metro" -> registry.getMapByTag("metro", playersList);
                case "halloween" -> registry.getMapByTag("halloween", playersList);
                default -> {
                    player.displayClientMessage(Component.literal("§7Неизвестная карта: " + mapTag), true);
                    yield null;
                }
            };
            for (PlayerPosition pp : playerPositions) {
                Player p = pp.Player;
                Vec3 ppos = pp.Position;

                // Если API использует Vec3d или BlockPos
                p.teleportTo(ppos.x, ppos.y, ppos.z); // или player.teleport(new Vec3d(pos.x, pos.y, pos.z));
                player.hurtMarked = true;
            }



            // Отменяем стандартное поведение (чтобы не открывался интерфейс)
            event.setCanceled(true);
        }

        /*if (!classTag.isEmpty()) {
            clearTags(player);

            switch (classTag) {
                case "bull" -> giveBullClass(player);
                case "ghost" -> giveGhostClass(player);
                case "lumberjack" -> giveLumberjackClass(player);
                case "knight" -> giveKnightClass(player);
                default -> {
                    player.displayClientMessage(Component.literal("§7Неизвестный класс: " + classTag), true);
                    return;
                }
            }

            // Отменяем стандартное поведение (чтобы не открывался интерфейс)
            event.setCanceled(true);
        }
*/
    }



    private static void giveKnightClass(Player player) {
        player.getInventory().clearContent();

        ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
// Цвет броне


// Вставляем в инвентарь игрока
        player.getInventory().armor.set(2, chestplate);

        player.getInventory().armor.set(3, new ItemStack(Items.IRON_HELMET));

        player.getInventory().armor.set(1, new ItemStack(Items.IRON_LEGGINGS));
        player.getInventory().armor.set(0, new ItemStack(Items.IRON_BOOTS));

        player.getInventory().add(new ItemStack(Items.IRON_SWORD));
        player.getInventory().add(new ItemStack(Items.SHIELD));
        player.getInventory().add(new ItemStack(Items.COOKED_BEEF, 16));

        player.addTag("knight");

        player.displayClientMessage(Component.literal("§7Вы выбрали класс §f[§6Рыцарь§f]!"), true);
    }

    private static void giveMageClass(Player player) {
        player.getInventory().clearContent();

        player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
        player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
        player.getInventory().armor.set(1, new ItemStack(Items.LEATHER_LEGGINGS));
        player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));

        player.getInventory().add(new ItemStack(Items.BLAZE_ROD));
        player.getInventory().add(new ItemStack(Items.ENCHANTED_BOOK));
        player.getInventory().add(new ItemStack(Items.GOLDEN_APPLE, 4));

        player.addTag("mage");
        player.displayClientMessage(Component.literal("§7Вы выбрали класс §f[§bМаг§f]!"), true);
    }

    private static void giveLumberjackClass(Player player) {
        player.getInventory().clearContent();

        player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
        player.getInventory().armor.set(2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        player.getInventory().armor.set(1, new ItemStack(Items.LEATHER_LEGGINGS));
        player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));

        player.getInventory().add(new ItemStack(Items.BOW));
        player.getInventory().add(new ItemStack(Items.ARROW, 64));
        player.getInventory().add(new ItemStack(Items.COOKED_BEEF, 12));

        player.addTag("archer");
        player.displayClientMessage(Component.literal("§7Вы выбрали класс §f[§aЛучник§f]!"), true);
    }
    private static void giveGhostClass(Player player) {
        player.getInventory().clearContent();

        player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
        player.getInventory().armor.set(2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        player.getInventory().armor.set(1, new ItemStack(Items.LEATHER_LEGGINGS));
        player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));

        player.getInventory().add(new ItemStack(Items.BOW));
        player.getInventory().add(new ItemStack(Items.ARROW, 64));
        player.getInventory().add(new ItemStack(Items.COOKED_BEEF, 12));

        player.addTag("archer");
        player.displayClientMessage(Component.literal("§7Вы выбрали класс §f[§aЛучник§f]!"), true);
    }
    private static void giveBullClass(Player player) {
        player.getInventory().clearContent();

        player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
        player.getInventory().armor.set(2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        player.getInventory().armor.set(1, new ItemStack(Items.LEATHER_LEGGINGS));
        player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));

        player.getInventory().add(new ItemStack(Items.BOW));
        player.getInventory().add(new ItemStack(Items.ARROW, 64));
        player.getInventory().add(new ItemStack(Items.COOKED_BEEF, 12));

        player.addTag("archer");
        player.displayClientMessage(Component.literal("§7Вы выбрали класс §f[§aЛучник§f]!"), true);
    }
    private static void clearTags(Player player){
        for (String tag : player.getTags()) {
            player.removeTag(tag);
        }
    }
}
