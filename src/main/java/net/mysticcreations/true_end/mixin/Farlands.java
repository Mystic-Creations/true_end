package net.mysticcreations.true_end.mixin;

import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PerlinNoise.class})
public class Farlands {
    @Inject(method={"wrap"}, at={@At(value="TAIL")}, cancellable=true)
    private static void injectMethod(double value, CallbackInfoReturnable<Double> cir) {
        cir.cancel();
        cir.setReturnValue(value);
    }
}