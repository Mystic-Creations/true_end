package net.justmili.trueend.procedures.randomevents;

import java.util.List;
import java.util.Random;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.TrueEnd.MODID;

@Mod.EventBusSubscriber
public class MobStare {
    private static int tickCounter = 0;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter >= 1) {
                tickCounter = 0;
                MobStare.execute(event.level);
            }
        }
    }

    public static void execute(LevelAccessor world) {
        if (!(world instanceof ServerLevel level))
            return;

        if (random.nextDouble() >= TrueEndVariables.randomEventChance.getValue())
            return;

        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            BlockPos playerPos = player.blockPosition();
            AABB area = new AABB(playerPos).inflate(32.0);
            List<Mob> mobs = level.getEntitiesOfClass(Mob.class, area);
            for (Mob mob : mobs) {
                if (mob.isAlive()) {
                    TrueEnd.LOGGER.info("MobStare Random Event Executed");
                    mob.getLookControl().setLookAt(player, 30.0f, 30.0f);
                }
            }
        }
    }
}
// Issues:
// Mobs don't freeze walking while looking
// The staring despite now working longer, it's not long enough (it's supposed to from like 10s to like 15min)
// Random chance should be hooked up to Math.random