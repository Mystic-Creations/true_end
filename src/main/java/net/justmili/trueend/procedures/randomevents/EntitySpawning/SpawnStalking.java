package net.justmili.trueend.procedures.randomevents.EntitySpawning;

import java.util.List;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Entities;
import net.justmili.trueend.network.Variables;

import net.justmili.trueend.procedures.advancement.NotAlone;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpawnStalking {
    private static final long TICK_INTERVAL = 1200L;
    private static final int MAX_ATTEMPTS = 16;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Level level = event.player.level();
        if (level.isClientSide() || !(level instanceof ServerLevel world)) return;
        if (Variables.MapVariables.get(world).isUnknownInWorld()) return;

        if (world.getGameTime() % TICK_INTERVAL != 0) return;
        if (world.dimension() == Level.END) return;
        if (!Variables.randomEventsToggle) return;

        long daysPlayed = world.getGameTime() / 24000;
        if (daysPlayed < 10) return;
        double difficultyMultiplier = switch (world.getDifficulty()) {
            case PEACEFUL -> 3.0;
            case EASY -> 0.5;
            case NORMAL -> 1.0;
            case HARD -> 2.0;
        };
        double spawnChance = Variables.entitySpawnChance * difficultyMultiplier * (daysPlayed - 10);
        spawnChance = Math.min(spawnChance, 1.0); //cap at 100%
        if (!(world.random.nextDouble() < (spawnChance))) return;

        List<ServerPlayer> players = world.players();
        if (players.isEmpty()) return;
        ServerPlayer player = players.get(world.random.nextInt(players.size()));
        double maxDistance = (world.getServer().getPlayerList().getViewDistance() * 16.0) - 48.0;
        if (maxDistance <= 48.0) return; //return if render distance too small

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            double angle = world.random.nextDouble() * Math.PI * 2.0;
            double dist = 32.0 + world.random.nextDouble() * (maxDistance - 32.0);
            double px = player.getX(), pz = player.getZ();
            int x = Mth.floor(px + Math.cos(angle) * dist);
            int z = Mth.floor(pz + Math.sin(angle) * dist);
            int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

            BlockPos groundPos = new BlockPos(x, y - 1, z);
            BlockState groundState = world.getBlockState(groundPos);
            if (groundState.isAir() || groundState.getBlock() == Blocks.WATER || groundState.getBlock() == Blocks.LAVA) {
                TrueEnd.LOGGER.debug("Attempt {}: invalid ground at {}: {}", attempt, groundPos, groundState.getBlock());
                continue;
            }

            EntityType<?> type = Entities.UNKNOWN.get();
            Entity entity = type.create(world);

            if (entity == null) return;
            entity.moveTo(x + 0.5, y, z + 0.5, world.random.nextFloat() * 360.0F, 0.0F);
            entity.getPersistentData().putBoolean("PersistenceRequired", true);
            entity.getPersistentData().putBoolean("doStalking", true); //Doesn't work so defaults to stalking,
                                                                                          // not a big deal in this case but annoying we didn't
                                                                                          // catch it earlier
            world.addFreshEntity(entity);

            Variables.MapVariables.get(world).setUnknownInWorld(true);
            NotAlone.grantAdvancement(player);
            TrueEnd.LOGGER.info("Spawned 'Unknown' at {} on {}", entity.blockPosition(), groundState);
            return;
        }
    }
}
