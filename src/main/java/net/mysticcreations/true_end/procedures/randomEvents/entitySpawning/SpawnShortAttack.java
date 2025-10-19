package net.mysticcreations.true_end.procedures.randomEvents.entitySpawning;

import net.mysticcreations.true_end.entity.Unknown;
import net.mysticcreations.true_end.init.Entities;
import net.mysticcreations.true_end.network.Variables;
import net.mysticcreations.true_end.procedures.advancement.NotAlone;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class SpawnShortAttack {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Level level = event.player.level();
        if (level.isClientSide() || !(level instanceof ServerLevel world)) return;
        if (Variables.MapVariables.get(world).isUnknownInWorld()) return;
        if (world.getGameTime() % 1200L != 0) return; //Tick Interval
        if (world.dimension() == Level.END) return;

        if (!Variables.doRandomEvents) return;
        if (!(world.random.nextDouble() < (Variables.entitySpawnChance / 50))) return;
        if (world.getDifficulty() == Difficulty.PEACEFUL) return;

        long daysPlayed = world.getGameTime() / 24000;
        if (daysPlayed < 24) return;

        List<ServerPlayer> players = world.players();
        if (players.isEmpty()) return;
        ServerPlayer player = players.get(world.random.nextInt(players.size()));

        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();

        EntityType<?> type = Entities.UNKNOWN.get();
        Entity entity = type.create(world);
        if (!(entity instanceof Unknown unknown)) return;
        entity.getPersistentData().putBoolean("PersistenceRequired", true);
        unknown.setBehavior(Unknown.UnknownBehavior.ATTACKING);

        //Random direction pick
        java.util.List<String> directions = new java.util.ArrayList<>();
        directions.add("EAST");
        directions.add("WEST");
        directions.add("SOUTH");
        directions.add("NORTH");
        java.util.Collections.shuffle(directions);

        for (String dir : directions) {
            if (world.random.nextDouble() >= 0.25) { continue; }

            double spawnX = playerX;
            double spawnZ = playerZ;

            spawnZ = switch (dir) {
                case "EAST" -> {
                    spawnX = playerX + 1.0;
                    yield playerZ;
                }
                case "WEST" -> {
                    spawnX = playerX - 1.0;
                    yield playerZ;
                }
                case "SOUTH" -> {
                    spawnX = playerX;
                    yield playerZ + 1.0;
                }
                case "NORTH" -> {
                    spawnX = playerX;
                    yield playerZ - 1.0;
                }
                default -> spawnZ;
            };

            double spawnY = playerY + 0.2;
            double entityDirZ = playerZ - spawnZ;
            double entityDirX = playerX - spawnX;
            float entityYaw = (float) (Math.toDegrees(Math.atan2(entityDirZ, entityDirX)) - 90.0);

            entity.moveTo(spawnX, spawnY, spawnZ, entityYaw, 0.0F);
            break;
        }

        //Spawn entity
        world.addFreshEntity(entity);
        if (!entity.isRemoved() && entity.level() == world) { //Make sure it won't set a false 'true'
            Variables.MapVariables.get(world).setUnknownInWorld(true);
            NotAlone.grantAdvancement(player);
        }
    }
}
