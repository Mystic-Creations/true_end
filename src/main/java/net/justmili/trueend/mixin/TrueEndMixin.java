package net.justmili.trueend.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TARGETCLASS.class)
public class TrueEndMixin {
	@Inject(at = @At("HEAD"), method = "")
	private void init(CallbackInfo info) {
	}
}