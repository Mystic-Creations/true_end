package net.mysticcreations.true_end.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

@Mixin(value = PerlinNoise.class, priority = 2048)
public class Farlands {
    @ModifyReturnValue(method = "wrap", at = @At("RETURN"))
    private static double replaceWrapReturn(double originalReturn, double input) {
        return input;
    }
}
