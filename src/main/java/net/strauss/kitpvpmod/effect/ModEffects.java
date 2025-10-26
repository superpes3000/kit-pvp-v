package net.strauss.kitpvpmod.effect;

import net.minecraftforge.eventbus.api.IEventBus;
import net.strauss.kitpvpmod.KitPvpMod;
import com.example.yourmod.effect.GhostEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, KitPvpMod.MOD_ID);

    public static final RegistryObject<MobEffect> GHOST = EFFECTS.register("ghost", GhostEffect::new);
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
