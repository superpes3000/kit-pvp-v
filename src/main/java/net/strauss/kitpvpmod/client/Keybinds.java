package net.strauss.kitpvpmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.strauss.kitpvpmod.KitPvpMod;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = KitPvpMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class Keybinds {

    public static final KeyMapping DASH_KEY =
            new KeyMapping("key.dashmod.dash", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.movement");

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(DASH_KEY);
    }
}
