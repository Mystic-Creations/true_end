package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.command.calls.screentests.TestBlackOverlay;
import net.justmili.trueend.init.Biomes;
import net.justmili.trueend.network.Variables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class BlackScreenSeepingReality {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide()) return;

        if (!player.level().getBiome(player.blockPosition()).is(Biomes.SEEPING_REALITY)) {
            player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> cap.setSeepingRealityTime(0));
            return;
        }
        if (!Variables.flashingLights) return;

        player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(cap -> {
            int t = cap.getSeepingRealityTime() + 1;
            cap.setSeepingRealityTime(t);

            final int PHASE1 = 3 * 60 * 20;    // 3600
            final int PHASE2 = 5 * 60 * 20;    // 6000
            final int PHASE3 = 10 * 60 * 20;   //12000
            final int PHASE4 = 15 * 60 * 20;   //18000
            final int MAX_REPEATS = 5;

            Random rng = new Random();

            if (t == PHASE1) {
                int repeats = 3 + rng.nextInt(MAX_REPEATS - 3 + 1);
                for (int i = 0; i < repeats; i++) {
                    TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                    int dur = rng.nextInt(3) + 1; // 1–3 ticks
                    TrueEnd.wait(dur, player::closeContainer);
                }
            }

            if (t == PHASE4) {
                int repeats = 3 + rng.nextInt(MAX_REPEATS - 3 + 1);
                for (int i = 0; i < repeats; i++) {
                    TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                    int d = rng.nextInt(3) + 1;  // 1–3 ticks
                    TrueEnd.wait(d, () -> {});
                }
                TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                TrueEnd.wait(100, () -> {
                    if (player instanceof ServerPlayer sp) {
                        ServerLevel sLevel = (ServerLevel) sp.level();
                        BlockPos resp = sp.getRespawnPosition();
                        if (resp == null) resp = sLevel.getSharedSpawnPos();
                        sp.teleportTo(sLevel, resp.getX() + 0.5, resp.getY() + 1, resp.getZ() + 0.5, sp.getYRot(), sp.getXRot()
                        );
                    }
                    cap.setSeepingRealityTime(0);
                });
                return;
            }

            if (t < PHASE1) return;

            double chance;
            int flashDur;
            if (t < PHASE2) {
                chance   = 0.0005;
                flashDur = rng.nextInt(3) + 1;
            } else if (t < PHASE3) {
                chance   = 0.001;
                flashDur = rng.nextInt(4) + 3;
            } else {
                chance   = 0.002;
                flashDur = rng.nextInt(6) + 2;
            }

            if (rng.nextDouble() < chance) {
                int repeats = 3 + rng.nextInt(MAX_REPEATS - 3 + 1);
                for (int i = 0; i < repeats; i++) {
                    TestBlackOverlay.execute(player.level(), player.getX(), player.getY(), player.getZ(), player);
                    TrueEnd.wait(flashDur, player::closeContainer);
                }
            }
        });
    }
}
