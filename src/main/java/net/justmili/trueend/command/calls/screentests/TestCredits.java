package net.justmili.trueend.command.calls.screentests;

import net.justmili.trueend.client.CreditsScreen;
import net.justmili.trueend.init.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TestCredits {
    public static void execute() {
        Minecraft mc = Minecraft.getInstance();
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
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
