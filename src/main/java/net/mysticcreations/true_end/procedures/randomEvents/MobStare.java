package net.mysticcreations.true_end.procedures.randomEvents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.mysticcreations.true_end.TrueEnd;
import net.mysticcreations.true_end.network.Variables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MobStare {
    private static final Map<Mob, Integer> stareMap = new HashMap<>();
    private static final Random RANDOM = new Random();

    private static long lastEventTick = 0;
    private static final long EVENT_COOLDOWN = 4L * 24000L;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        LevelAccessor world = event.level;
        if (!(world instanceof ServerLevel server)) return;

        long worldTick = server.getGameTime();
        if (worldTick < lastEventTick + EVENT_COOLDOWN) {
            updateStare(server);
            return;
        }
        if (Variables.doRandomEvents) {
            if (stareMap.isEmpty() && RANDOM.nextDouble() < Variables.randomEventChance) {
                lastEventTick = worldTick;
                TrueEnd.LOGGER.info("[MobStare] Starting stare event");

                List<ServerPlayer> players = server.players();
                int maxDuration = 0;
                for (ServerPlayer player : players) {
                    AABB area = new AABB(player.blockPosition()).inflate(32.0);
                    for (Mob mob : server.getEntitiesOfClass(Mob.class, area)) {
                        int duration = 200 + RANDOM.nextInt(18000 - 200 + 1);
                        stareMap.put(mob, duration);
                        if (duration > maxDuration) maxDuration = duration;
                    }
                }
                double seconds = maxDuration / 20.0;
                String human = seconds >= 60 ? String.format("%.1f minutes", seconds / 60.0)
                        : String.format("%.1f seconds", seconds);
                TrueEnd.LOGGER.info("MobStare event executed / Lasting {} ticks (~{})", maxDuration, human);
            }
        }
        updateStare(server);
    }

    private static void updateStare(ServerLevel server) {
        Iterator<Map.Entry<Mob, Integer>> iter = stareMap.entrySet().iterator();
        List<ServerPlayer> players = server.players();
        while (iter.hasNext()) {
            Map.Entry<Mob, Integer> entry = iter.next();
            Mob mob = entry.getKey();
            int ticksLeft = entry.getValue() - 1;
            if (ticksLeft <= 0) {
                iter.remove();
                continue;
            }
            entry.setValue(ticksLeft);

            // Freeze movement
            PathNavigation nav = mob.getNavigation();
            nav.stop();

            // Look at nearest player
            ServerPlayer closest = null;
            double bestDist = Double.MAX_VALUE;
            for (ServerPlayer p : players) {
                double d = mob.distanceTo(p);
                if (d < bestDist) {
                    bestDist = d;
                    closest = p;
                }
            }
            if (closest != null) {
                mob.getLookControl().setLookAt(closest, 30.0F, 30.0F);
            }
        }
    }
}
