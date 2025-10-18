package net.kaupenjoe.tutorialmod.item;

import com.example.mod.items.KnightAssault;
import net.kaupenjoe.tutorialmod.TutorialMod;
import net.kaupenjoe.tutorialmod.item.abilities.*;
import net.kaupenjoe.tutorialmod.item.bows.Fox5BOW;
import net.kaupenjoe.tutorialmod.item.custom.CrowBurst;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MOD_ID);


    public static final RegistryObject<Item> DASH_ITEM = ITEMS.register("crow_dash",
            () -> new CrowDash(new Item.Properties()));
    public static final RegistryObject<Item> FOX5_BOW = ITEMS.register("fox5_bow",
            () -> new Fox5BOW(new Item.Properties()));
    public static final RegistryObject<Item> TOMAHAWK = ITEMS.register("tomahawk",
            () -> new net.kaupenjoe.tutorialmod.item.custom.TomahawkItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GRENADE = ITEMS.register("grenade",
            () -> new GrenadeItem(new Item.Properties().stacksTo(16)));


    public static final RegistryObject<Item> CROW_ATTACK = ITEMS.register("crow_attack",
            () -> new TripleArrowItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> CROW_BURST = ITEMS.register("crow_burst",
            () -> new CrowBurst(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SPIN_ATTACK = ITEMS.register("spin_attack",
            () -> new com.example.mod.items.SpinAttack(new Item.Properties()));

    public static final RegistryObject<Item> KNIGHT_ASSAULT = ITEMS.register("knight_assault",
            () -> new KnightAssault(new Item.Properties()));
    public static final RegistryObject<Item> TOMAHAWK_ESCAPE = ITEMS.register("tomahawk_escape",
            () -> new TomahawkEscape(new Item.Properties()));
    public static final RegistryObject<Item> LUMBER_RUSH = ITEMS.register("lumber_rush",
            () -> new LumberRush(new Item.Properties()));

    public static final RegistryObject<Item> BEAST_RUSH = ITEMS.register("beast_rush",
            () -> new BeastRush(new Item.Properties()));

    public static final RegistryObject<Item> JUMP_SLAM = ITEMS.register("jump_slam",
            () -> new JumpSlam(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
