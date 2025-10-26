package net.strauss.kitpvpmod.item;

import net.strauss.kitpvpmod.item.abilities.*;
import net.strauss.kitpvpmod.KitPvpMod;
import net.strauss.kitpvpmod.item.bows.Fox5BOW;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KitPvpMod.MOD_ID);


    public static final RegistryObject<Item> DASH_ITEM = ITEMS.register("crow_dash",
            () -> new CrowDash(new Item.Properties()));
    public static final RegistryObject<Item> FOX5_BOW = ITEMS.register("fox5_bow",
            () -> new Fox5BOW(new Item.Properties()));
    public static final RegistryObject<Item> TOMAHAWK = ITEMS.register("tomahawk",
            () -> new TomahawkItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GRENADE = ITEMS.register("grenade",
            () -> new GrenadeItem(new Item.Properties().stacksTo(16)));


    public static final RegistryObject<Item> CROW_ATTACK = ITEMS.register("crow_attack",
            () -> new TripleArrowItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> CROW_BURST = ITEMS.register("crow_burst",
            () -> new CrowBurst(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SPIN_ATTACK = ITEMS.register("spin_attack",
            () -> new SpinAttack(new Item.Properties()));

    public static final RegistryObject<Item> KNIGHT_ASSAULT = ITEMS.register("knight_assault",
            () -> new KnightAssault(new Item.Properties()));

    public static final RegistryObject<Item> KNIGHT_ESTUS = ITEMS.register("knight_estus",
            () -> new KnightEstus(new Item.Properties()));

    public static final RegistryObject<Item> TOMAHAWK_ESCAPE = ITEMS.register("tomahawk_escape",
            () -> new TomahawkEscape(new Item.Properties()));
    public static final RegistryObject<Item> LUMBER_RUSH = ITEMS.register("lumber_rush",
            () -> new LumberRush(new Item.Properties()));
    public static final RegistryObject<Item> NURSE_BLINK = ITEMS.register("nurse_blink",
            () -> new NurseBlink(new Item.Properties()));
    public static final RegistryObject<Item> INVISIBILITY_ITEM = ITEMS.register("invisibility_item",
            () -> new com.example.mymod.item.InvisibilityItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BEAST_RUSH = ITEMS.register("beast_rush",
            () -> new BeastRush(new Item.Properties()));

    public static final RegistryObject<Item> JUMP_SLAM = ITEMS.register("jump_slam",
            () -> new JumpSlam(new Item.Properties()));

    public static final RegistryObject<Item> HOOK = ITEMS.register("hook",
            () -> new HookItem(new Item.Properties()));

    public static final RegistryObject<Item> SUMMON_SKELETONS_ITEM = ITEMS.register("summon_skeletons_item",
            () -> new SummonSkeletonsItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SWORD_PROJECTILE = ITEMS.register("sword_projectile",
            () -> new com.example.mod.item.SwordThrowItem(new Item.Properties()));

    public static final RegistryObject<Item> GHOST_INVISIBILITY = ITEMS.register("ghost_invisibility",
            () -> new GhostInvisibility(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
