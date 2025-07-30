package net.justmili.trueend.command.calls.screentests;

import net.justmili.trueend.client.CreditsScreen;
import net.justmili.trueend.init.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TestCredits {
    private static final Minecraft mc = Minecraft.getInstance();
    public static void execute(double x, double y, double z) {
        mc.execute(() -> {
            mc.getSoundManager().stop();

            if (mc.level != null && mc.player != null) {
                mc.getSoundManager().stop();
                if (mc.level != null && mc.player != null) {
                    mc.level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(Sounds.MOD_CREDITS_MUSIC.getId())), SoundSource.MASTER, 1, 1, false);
                }
                mc.execute(() -> mc.setScreen(new CreditsScreen()));
            }
        });
    }
}
