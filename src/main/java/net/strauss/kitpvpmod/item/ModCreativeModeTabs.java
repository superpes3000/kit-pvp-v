package net.strauss.kitpvpmod.item;

import net.strauss.kitpvpmod.KitPvpMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KitPvpMod.MOD_ID);


    public static final RegistryObject<CreativeModeTab> KIT_PVP_V_ITEMS = CREATIVE_MODE_TABS.register("kit_pvp_v_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SPIN_ATTACK.get()))

                    .title(Component.literal("Kit PvP V"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SPIN_ATTACK.get());
                        output.accept(ModItems.KNIGHT_ASSAULT.get());
                        output.accept(ModItems.TOMAHAWK.get());
                        output.accept(ModItems.TOMAHAWK_ESCAPE.get());
                        output.accept(ModItems.LUMBER_RUSH.get());
                        output.accept(ModItems.BEAST_RUSH.get());
                        output.accept(ModItems.JUMP_SLAM.get());
                        output.accept(ModItems.FOX5_BOW.get());
                        output.accept(ModItems.GRENADE.get());
                        output.accept(ModItems.CROW_ATTACK.get());
                        output.accept(ModItems.CROW_BURST.get());
                        output.accept(ModItems.DASH_ITEM.get());
                        output.accept(ModItems.HOOK.get());
                        output.accept(ModItems.NURSE_BLINK.get());
                        output.accept(ModItems.INVISIBILITY_ITEM.get());
                        output.accept(ModItems.SUMMON_SKELETONS_ITEM.get());
                        output.accept(ModItems.SWORD_PROJECTILE.get());
                        output.accept(ModItems.GHOST_INVISIBILITY.get());
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
