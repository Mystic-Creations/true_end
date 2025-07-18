package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TimeChange {
    private static final int DAY = 1000;
    private static final int NOON = 6000;
    private static final int NIGHT = 13000;
    private static final int MIDNIGHT = 18000;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!Variables.randomEventsToggle) return;
        if (!(event.player instanceof ServerPlayer serverPlayer)) return;

        makeNight(serverPlayer);
        makeDay(serverPlayer);
    }

    public static void makeNight(ServerPlayer player) {
        if (!(Math.random() < (Variables.randomEventChance/8))) return;
        ServerLevel world = (ServerLevel) player.level();
        long time = world.getDayTime() % 24000;
        if (time > DAY && time < NIGHT) {
            long newTime = NIGHT + (long) (Math.random() * (MIDNIGHT - NIGHT));
            long total = world.getDayTime() - time + newTime;
            world.setDayTime(total);
        }
    }

    public static void makeDay(ServerPlayer player) {
        if (!(Math.random() < (Variables.randomEventChance/8))) return;
        ServerLevel world = (ServerLevel) player.level();
        long time = world.getDayTime() % 24000;
        boolean isNight = time >= MIDNIGHT || time < DAY;
        if (isNight) {
            long newTime = DAY + (long) (Math.random() * (NOON - DAY));
            long total = world.getDayTime() - time + newTime;
            world.setDayTime(total);
        }
    }
}
