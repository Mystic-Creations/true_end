package net.mysticcreations.true_end.init;

import net.mysticcreations.true_end.client.gui.renderers.Funny;
import net.mysticcreations.true_end.client.gui.renderers.BlackOverlay;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraft.world.inventory.MenuType;
import net.mysticcreations.true_end.TrueEnd;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Guis {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TrueEnd.MODID);
    public static final RegistryObject<MenuType<net.mysticcreations.true_end.client.gui.inventory.Funny>> FUNNY = REGISTRY.register("funny", () -> IForgeMenuType.create(net.mysticcreations.true_end.client.gui.inventory.Funny::new));
    public static final RegistryObject<MenuType<net.mysticcreations.true_end.client.gui.inventory.BlackOverlay>> BLACK_SCREEN = REGISTRY.register("black_screen", () -> IForgeMenuType.create(net.mysticcreations.true_end.client.gui.inventory.BlackOverlay::new));

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Guis.FUNNY.get(), Funny::new);
            MenuScreens.register(Guis.BLACK_SCREEN.get(), BlackOverlay::new);
        });
    }
}
