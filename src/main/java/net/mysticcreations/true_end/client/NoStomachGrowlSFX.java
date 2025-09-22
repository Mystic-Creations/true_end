package net.mysticcreations.true_end.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class NoStomachGrowlSFX {
    private static final ResourceLocation STOMACH_GROWL = ResourceLocation.parse("subtle_effects:entity.player.stomach_growl");

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        var soundInstance = event.getSound();
        if (soundInstance == null) return;

        ResourceLocation id = soundInstance.getLocation();
        if (STOMACH_GROWL.equals(id)) {
            event.setSound(null);
        }
    }
}
