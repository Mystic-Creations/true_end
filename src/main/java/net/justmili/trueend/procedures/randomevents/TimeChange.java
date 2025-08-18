package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TimeChange {
    public static final int DAY = 1000;
    public static final int NOON = 6000;
    public static final int NIGHT = 13000;
    public static final int MIDNIGHT = 18000;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!Variables.randomEventsToggle) return;
        if (!Variables.daytimeChangeToggle) return;

        ServerLevel world = (ServerLevel) event.level;
        long totalDays = world.getDayTime() / 24000;
        if (totalDays < 3) return;
        if (totalDays % 4 != 0) return;

        makeNight(world);
        makeDay(world);
    }

    public static void makeNight(ServerLevel world) {
        if (!(Math.random() < (Variables.randomEventChance/64))) return;
        long time = world.getDayTime() % 24000;
        if (time > DAY && time < NIGHT) {
            long newTime = NIGHT + (long) (Math.random() * (MIDNIGHT - NIGHT));
            long total = world.getDayTime() - time + newTime;
            world.setDayTime(total);
        }
    }

    public static void makeDay(ServerLevel world) {
        if (!(Math.random() < (Variables.randomEventChance/64))) return;
        long time = world.getDayTime() % 24000;
        boolean isNight = time >= MIDNIGHT || time < DAY;
        if (isNight) {
            long newTime = DAY + (long) (Math.random() * (NOON - DAY));
            long total = world.getDayTime() - time + newTime;
            world.setDayTime(total);
        }
    }
}
