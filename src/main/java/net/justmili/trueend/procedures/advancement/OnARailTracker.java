package net.justmili.trueend.procedures.advancement;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class OnARailTracker {
    private static final Map<UUID, BlockPos> startPositions = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.getVehicle() instanceof AbstractMinecart) {
            UUID playerId = player.getUUID();
            BlockPos currentPos = player.blockPosition();

            if (!startPositions.containsKey(playerId)) {
                startPositions.put(playerId, currentPos);
            } else {
                BlockPos startPos = startPositions.get(playerId);
                int distance = Math.abs(currentPos.getX() - startPos.getX());
                if (distance >= 1000) {
                    grantAdvancement(player);
                    startPositions.remove(playerId);
                }
            }
        } else {
            startPositions.remove(player.getUUID());
        }
    }

    private static void grantAdvancement(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ResourceLocation advancementId = ResourceLocation.parse("true_end:story/travel_in_minecart");
            var advancement = serverPlayer.server.getAdvancements().getAdvancement(advancementId);
            if (advancement != null) {
                var progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                if (!progress.isDone()) {
                    for (String criterion : progress.getRemainingCriteria()) {
                        serverPlayer.getAdvancements().award(advancement, criterion);
                    }
                }
            }
        }
    }
}