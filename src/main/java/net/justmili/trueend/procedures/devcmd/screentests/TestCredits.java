package net.justmili.trueend.procedures.devcmd.screentests;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.client.TrueEndCreditsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class TestCredits {


    public static void execute(Level world, double x, double y, double z) {
        TrueEnd.queueServerWork(2, () -> {
            Minecraft mc = Minecraft.getInstance();
            mc.getSoundManager().stop();

            if (mc.level != null && mc.player != null) {
                mc.level.playLocalSound(
                        mc.player.getX(),
                        mc.player.getY(),
                        mc.player.getZ(),
                        SoundEvents.MUSIC_DISC_PIGSTEP,
                        SoundSource.MUSIC,
                        1.0f,
                        1.0f,
                        false
                );
                TrueEnd.queueServerWork(2, () -> {
                    if (mc.screen == null && mc.player != null) {
                        mc.setScreen(new TrueEndCreditsScreen());
                    }
                });
            }
        });
    }
}
