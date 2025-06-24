package net.justmili.trueend.init;

import net.justmili.trueend.client.gui.FunnyScreen;
import net.justmili.trueend.world.inventory.BlackScreenMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import net.justmili.trueend.world.inventory.FunnyMenu;
import net.justmili.trueend.TrueEnd;

public class Guis {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TrueEnd.MODID);
    public static final RegistryObject<MenuType<FunnyMenu>> FUNNY = REGISTRY.register("funny", () -> IForgeMenuType.create(FunnyMenu::new));
    public static final RegistryObject<MenuType<BlackScreenMenu>> BLACK_SCREEN = REGISTRY.register("black_screen", () -> IForgeMenuType.create(BlackScreenMenu::new));

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Guis.FUNNY.get(), FunnyScreen::new);
        });
    }
}
