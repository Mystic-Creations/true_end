package net.justmili.trueend.world.dimension;

import net.justmili.trueend.network.TrueEndVariables;
import static net.justmili.trueend.network.TrueEndVariables.fogToggle;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.DimensionSpecialEffects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeyondTheDream {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        DimensionSpecialEffects customEffect = new DimensionSpecialEffects(112f, true, DimensionSpecialEffects.SkyType.NONE, false, false) {
            
            @Override
            public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
                return color;
            }

            public boolean isFoggyAt(int x, int y, int z) {
                return (fogToggle != null && fogToggle.getValue());
            }

            @Override
            public boolean isFoggyAt(int p_108874_, int p_108875_) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        event.register(new ResourceLocation("true_end", "beyond_the_dream"), customEffect);
    }
}