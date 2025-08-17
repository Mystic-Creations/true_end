package net.justmili.trueend.procedures.randomevents;

import java.util.List;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Entities;
import net.justmili.trueend.network.Variables;

import net.justmili.trueend.procedures.advancement.NotAlone;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
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
public class EntitySpawning {
    private static final long TICK_INTERVAL = 1200L;
    private static final int MAX_ATTEMPTS = 16;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Level level = event.level;
        if (level.isClientSide() || !(level instanceof ServerLevel world)) return;

        if (world.getGameTime() % TICK_INTERVAL != 0) return;
        if (world.dimension() == Level.END) return;
        if (!Variables.randomEventsToggle) return;

        double chanceMultiplier = 0.0;
        Difficulty difficulty = world.getDifficulty();
        if (difficulty == Difficulty.PEACEFUL) {
            chanceMultiplier = 3.0; // :)
        } else if (difficulty == Difficulty.EASY) {
            chanceMultiplier = 0.5;
        } else if (difficulty == Difficulty.NORMAL) {
            chanceMultiplier = 1.0;
        } else if (difficulty == Difficulty.HARD) {
            chanceMultiplier = 2.0;
        }

        if (!(Math.random() < (Variables.entitySpawnChance * chanceMultiplier))) return;

        List<ServerPlayer> players = world.players();
        if (players.isEmpty()) return;
        ServerPlayer player = players.get(world.random.nextInt(players.size()));
        double maxDistance = (world.getServer().getPlayerList().getViewDistance() * 16.0) - 48.0;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            double angle = world.random.nextDouble() * Math.PI * 2.0;
            double dist = 32.0 + world.random.nextDouble() * (maxDistance - 32.0);
            double px = player.getX(), pz = player.getZ();
            int x = Mth.floor(px + Math.cos(angle) * dist);
            int z = Mth.floor(pz + Math.sin(angle) * dist);
            int surfaceY = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

            BlockPos groundPos = new BlockPos(x, surfaceY - 1, z);
            BlockState groundState = world.getBlockState(groundPos);
            if (groundState.isAir() || groundState.getBlock() == Blocks.WATER || groundState.getBlock() == Blocks.LAVA) {
                TrueEnd.LOGGER.debug("Attempt {}: invalid ground at {}: {}", attempt, groundPos, groundState.getBlock());
                continue;
            }

            EntityType<?> type = Entities.UNKNOWN.get();
            Entity entity = type.create(world);

            if (entity == null) return;
            entity.moveTo(x + 0.5, surfaceY, z + 0.5, world.random.nextFloat() * 360.0F, 0.0F);
            entity.getPersistentData().putBoolean("PersistenceRequired", true);
            entity.getPersistentData().putBoolean("doStalking", true);
            world.addFreshEntity(entity);

            Variables.MapVariables.get(world).setUnknownInWorld(true);
            NotAlone.grantAdvancement(player);
            TrueEnd.LOGGER.info("Spawned 'Unknown' at {} on {} after {} attempts.", entity.blockPosition(), groundState, attempt + 1);
            return;
        }
    }
}
