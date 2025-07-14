package net.justmili.trueend.client;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.entity.renderer.UnknownEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "true_end", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        TrueEnd.EVENT_BUS.addListener(UnknownEntityRenderer::registerEntityRenderers);
    }
}
