package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.interfaces.User32;
import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SleepPopup {
    @SubscribeEvent
    public static void onPlayerInBed(PlayerSleepInBedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Level level = player.level();
        if (!level.isClientSide) return;

        if (!Variables.randomEventsToggle) return;
        if (!Variables.popupsToggle) return;
        if (!(Math.random() < Variables.randomEventChance * 1.5)) return;
        if (level.dimension() != Level.OVERWORLD) return;

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!player.isSleeping()) return;
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                User32.INSTANCE.MessageBoxA(0L, "wake up.", "", 0);
            }
            //We'll do this shit for Linux and MacOS some other time
        }).start();
    }
}