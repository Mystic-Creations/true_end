package net.justmili.trueend.config.handler;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.config.Config;
@Mod.EventBusSubscriber(modid = TrueEnd.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfigHandler {
    @SubscribeEvent
    public static void onConstruct(FMLConstructModEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get()
                    .registerExtensionPoint(ConfigScreenFactory.class,
                            () -> new ConfigScreenFactory((mc, parent) -> buildScreen(parent)));
        }
    }

    private static net.minecraft.client.gui.screens.Screen buildScreen(net.minecraft.client.gui.screens.Screen parent) {
        ConfigBuilder builder = Config.getConfigBuilder()
                .setParentScreen(parent);
        return builder.build();
    }
}
