package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.List;
import net.minecraftforge.event.TickEvent.LevelTickEvent;

@Mod.EventBusSubscriber
public class EntitySpawning {
    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event);
        }
    }

    public static void execute() {
        execute(null);
    }

    private static void execute(Event event) {
        if (!(event instanceof LevelTickEvent lvlEvent)) {
            return;
        }

        Level lvl = lvlEvent.level;
        if (lvl.isClientSide()) {
            return;
        }
        ServerLevel world = (ServerLevel) lvl;
        long gameTicks = world.getGameTime();
        if (gameTicks % 100L != 0L) {
            return;
        }

        if (TrueEndVariables.randomEventsToggle.getValue() == true) {
            if (Math.random() < TrueEndVariables.entitySpawnChance.getValue()) {
                if (!TrueEndVariables.MapVariables.get(world).isUnknownInWorld()) {
                    System.out.println("[DEBUG] true_end: Searching for 'Unknown' spawn position...");

                    List<ServerPlayer> players = world.players();
                    if (players.isEmpty()) {
                        return;
                    }
                    ServerPlayer player = players.get(world.getRandom().nextInt(players.size()));

                    double px = player.getX();
                    double pz = player.getZ();

                    double angle = world.getRandom().nextDouble() * Math.PI * 2.0;

                    double minDist = 64.0;
                    double maxDist = 95.0;
                    double distance = minDist + world.getRandom().nextDouble() * (maxDist - minDist);

                    int spawnX = Mth.floor(px + Math.cos(angle) * distance);
                    int spawnZ = Mth.floor(pz + Math.sin(angle) * distance);
                    int spawnY = world.getHeight(Heightmap.Types.MOTION_BLOCKING, spawnX, spawnZ);

                    BlockPos groundPos = new BlockPos(spawnX, spawnY, spawnZ);
                    Block groundBlock = world.getBlockState(groundPos).getBlock();

                    Block grassBlock = ForgeRegistries.BLOCKS.getValue(
                            ResourceLocation.parse("true_end:grass_block"));
                    Block stoneBlock = ForgeRegistries.BLOCKS.getValue(
                            ResourceLocation.parse("true_end:stone"));
                    Block sandBlock = ForgeRegistries.BLOCKS.getValue(
                            ResourceLocation.parse("true_end:sand"));
                    if (groundBlock != grassBlock && groundBlock != stoneBlock && groundBlock != sandBlock) {
                        System.out.println(
                                "[DEBUG] true_end: Entity spawn failed, attempted to spawn on blacklisted block."
                        );
                        return;
                    }

                    BlockPos posOne = new BlockPos(spawnX, spawnY + 1, spawnZ);
                    BlockPos posTwo = new BlockPos(spawnX, spawnY + 2, spawnZ);
                    if (world.isEmptyBlock(posOne) && world.isEmptyBlock(posTwo)) {
                        EntityType<?> unknownType = ForgeRegistries.ENTITY_TYPES.getValue(
                                ResourceLocation.parse("true_end:unknown"));
                        if (unknownType != null) {
                            Entity unknownEntity = unknownType.create(world);
                            if (unknownEntity != null) {
                                unknownEntity.moveTo(spawnX + 0.5, spawnY + 1.0, spawnZ + 0.5,
                                        world.getRandom().nextFloat() * 360.0F,
                                        0.0F);

                                world.addFreshEntity(unknownEntity);

                                System.out.println(
                                        "[DEBUG] true_end: Spawned 'unknown' at X=" + spawnX + ", Y=" + (spawnY + 1) + ", Z=" + spawnZ
                                );
                                TrueEndVariables.MapVariables.get(world).setUnknownInWorld(true);
                            }
                        }
                    }
                } else {
                    System.out.println(
                            "[DEBUG] true_end: Attempted to spawn 'Unknown' but failed, entity already in the world"
                    );
                }
            }
        }
    }
}
