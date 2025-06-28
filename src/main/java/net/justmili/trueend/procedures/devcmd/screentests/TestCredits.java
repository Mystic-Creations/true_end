package net.justmili.trueend.procedures.devcmd.screentests;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.client.CreditsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

import static net.justmili.trueend.procedures.events.PlayCredits.playSound;

public class TestCredits {


    public static void execute(Level world, double x, double y, double z) {
        TrueEnd.queueServerWork(2, () -> {
            Minecraft mc = Minecraft.getInstance();
            mc.getSoundManager().stop();

            if (mc.level != null && mc.player != null) {
                playSound(x, y, z);
                TrueEnd.queueServerWork(2, () -> {
                    if (mc.screen == null && mc.player != null) {
                        mc.setScreen(new CreditsScreen());
                    }
                });
            }
        });
    }
}
