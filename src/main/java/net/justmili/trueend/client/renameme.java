package net.justmili.trueend.client;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CompassItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(
    modid = TrueEnd.MODID,
    value = Dist.CLIENT,
    bus   = Mod.EventBusSubscriber.Bus.MOD
)
public class renameme {

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                Items.DREAMERS_COMPASS.get(),
                new ResourceLocation(TrueEnd.MODID, "angle"),
                (stack, world, entity, seed) -> {
                    if (world == null || entity == null) {
                        return 0F;
                    }
                    return CompassItem.getLodestoneAngle(stack, world, entity);
                }
            );
        });
    }

    static {
        FMLJavaModLoadingContext
            .get()
            .getModEventBus()
            .addListener(ClientSetup::onClientSetup);
    }
}
