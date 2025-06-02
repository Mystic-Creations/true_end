package net.justmili.trueend.world.dimension;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class BeyondTheDream {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BeyondTheDreamSpecialEffectsHandler {
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
            DimensionSpecialEffects customEffect = new DimensionSpecialEffects(112f, true, DimensionSpecialEffects.SkyType.NORMAL, false, false) {
                @Override
                public @NotNull Vec3 getBrightnessDependentFogColor(@NotNull Vec3 color, float sunHeight) {
                    return color;
                }

                @Override
                public boolean isFoggyAt(int x, int y) {
                    return TrueEndVariables.fogToggle.getValue();
                }
            };
            event.register(ResourceLocation.parse("true_end:beyond_the_dream"), customEffect);
        }
    }
}