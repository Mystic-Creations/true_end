package net.justmili.trueend.procedures;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.justmili.trueend.init.TrueEndGameRules;
import net.justmili.trueend.init.TrueEndBlocks;
import net.justmili.trueend.TrueEnd;
import net.minecraft.world.effect.MobEffectInstance;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;
import static net.justmili.trueend.regs.IntegerRegistry.*;

@Mod.EventBusSubscriber
public class DimSwapToBTD {
    private static final Map<ServerPlayer, Boolean> HAS_PROCESSED = new HashMap<>();

    public static final int HOUSE_PLATEAU_WIDTH = 9;
    public static final int HOUSE_PLATEAU_LENGTH = 7;
    public static final int TERRAIN_ADAPT_EXTENSION = 10;

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (event.getAdvancement().getId().equals(ResourceLocation.parse("true_end:stop_dreaming"))) {
            execute(event, event.getEntity().level(), event.getEntity());
        }
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (!(entity instanceof ServerPlayer serverPlayer))
            return;

        if (HAS_PROCESSED.getOrDefault(serverPlayer, false))
            return;

        AtomicBoolean hasVisited = new AtomicBoolean(false);
        serverPlayer.getCapability(TrueEndVariables.PLAYER_VARS_CAP).ifPresent(data -> {
            hasVisited.set(data.hasBeenBeyond());
        });
        if (!hasVisited.get()) {
            if (serverPlayer.level().dimension() == Level.OVERWORLD
                    && serverPlayer.level() instanceof ServerLevel overworld
                    && serverPlayer.getAdvancements().getOrStartProgress(
                            Objects.requireNonNull(serverPlayer.server.getAdvancements()
                                    .getAdvancement(ResourceLocation.parse("true_end:stop_dreaming"))))
                            .isDone()) {

                HAS_PROCESSED.put(serverPlayer, true);

                ServerLevel nextLevel = serverPlayer.server.getLevel(BTD);
                if (nextLevel == null || serverPlayer.level().dimension() == BTD) {
                    HAS_PROCESSED.remove(serverPlayer);
                    return;
                }

                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

                TrueEnd.queueServerWork(5, () -> {
                    BlockPos worldSpawn = overworld.getSharedSpawnPos();
                    BlockPos initialSearchPos = TrueEnd.locateBiome(nextLevel, worldSpawn, "true_end:nostalgic_meadow");
                    if (initialSearchPos == null)
                        initialSearchPos = worldSpawn;

                    BlockPos spawnPos = findIdealSpawnPoint(nextLevel, initialSearchPos);

                    BlockPos secondarySearchPos = TrueEnd.locateBiome(nextLevel,
                            new BlockPos(new Vec3i(BlockPosRandomX, BlockPosRandomY, BlockPosRandomZ)), "true_end:nostalgic_meadow");

                    if (spawnPos == null) {
                        while (spawnPos == null) {
                            secondarySearchPos =
                                    new BlockPos(new Vec3i(BlockPosRandomX + BlockPosRandomZ,
                                    BlockPosRandomY,
                                    BlockPosRandomZ + BlockPosRandomX));

                            spawnPos = findFallbackSpawn(nextLevel, secondarySearchPos);
                        }
                    }

                    if (spawnPos == null) {
                        System.out.println("[DEBUG] true_end: Could not find ANY fallback spawn point!");
                        spawnPos = nextLevel.getSharedSpawnPos();
                    }

                    BlockPos finalSpawnPos = spawnPos;
                    BlockPos secFinalSpawnPos = secondarySearchPos;

                    serverPlayer.teleportTo(nextLevel, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                            serverPlayer.getYRot(), serverPlayer.getXRot());
                    serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                    for (MobEffectInstance effect : serverPlayer.getActiveEffects())
                        serverPlayer.connection
                                .send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), effect));
                    serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));

                    TrueEnd.queueServerWork(5, () -> {
                        removeNearbyTrees(nextLevel, serverPlayer.blockPosition(), 15);
                        adaptTerrain(nextLevel, serverPlayer.blockPosition());
                        executeCommand(nextLevel, serverPlayer, "function true_end:build_home");
                        sendFirstEntryConversation(serverPlayer, nextLevel);
                        serverPlayer.getCapability(TrueEndVariables.PLAYER_VARS_CAP)
                                .ifPresent(data -> data.setBeenBeyond(true));
                        if (secFinalSpawnPos == null) {
                            nextLevel.getCapability(TrueEndVariables.MAP_VARIABLES_CAP).ifPresent(
                                    data -> data.setBtdSpawn(finalSpawnPos.getX(), finalSpawnPos.getY(),
                                            finalSpawnPos.getZ()));
                        }
                        if (secFinalSpawnPos != null) {
                            nextLevel.getCapability(TrueEndVariables.MAP_VARIABLES_CAP).ifPresent(
                                    data -> data.setBtdSpawn(secFinalSpawnPos.getX(), secFinalSpawnPos.getY(),
                                            secFinalSpawnPos.getZ()));
                        }
                        HAS_PROCESSED.remove(serverPlayer);
                    });

                    if (nextLevel.getGameRules().getBoolean(TrueEndGameRules.CLEAR_DREAM_ITEMS)) {
                        serverPlayer.getInventory().clearContent();
                        nextLevel.getGameRules().getRule(TrueEndGameRules.CLEAR_DREAM_ITEMS).set(false,
                                nextLevel.getServer());
                    }
                });
            }
        }
    }

    public static void removeNearbyTrees(ServerLevel level, BlockPos center, int radius) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        // Initial scan around the center to enqueue leaves/logs
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockState state = level.getBlockState(mutablePos);
                    Block block = state.getBlock();

                    if (isTreeBlock(block)) {
                        BlockPos foundPos = mutablePos.immutable();
                        level.removeBlock(foundPos, true);
                        queue.add(foundPos);
                        visited.add(foundPos);
                    }
                }
            }
        }

        // Breadth-first loop to remove connected tree blocks
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            for (Direction direction : Direction.values()) {
                BlockPos neighbor = current.relative(direction);

                if (!visited.contains(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    Block neighborBlock = neighborState.getBlock();

                    if (isTreeBlock(neighborBlock)) {
                        level.removeBlock(neighbor, true);
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
    }

    // Helper method to identify tree blocks (leaves or logs)
    private static boolean isTreeBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.LEAVES) || block.defaultBlockState().is(BlockTags.LOGS);
    }

    public static BlockPos findIdealSpawnPoint(ServerLevel level, BlockPos centerPos) {
        int searchRadius = 24;
        for (int y = 75; y >= 64; y--) {
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos candidate = centerPos.offset(x, y - centerPos.getY(), z);
                    BlockPos above = candidate.above();

                    BlockPos above2 = above.above();
                    if (level.getBlockState(candidate).is(TrueEndBlocks.GRASS_BLOCK.get())
                            && level.getBiome(candidate).is(ResourceLocation.parse("true_end:nostalgic_meadow"))
                            && isYInSpawnRange(level, candidate)
                            && noBadBlocks(level, candidate)
                            && level.isEmptyBlock(above)
                            && level.isEmptyBlock(above2)
                            && level.getBrightness(LightLayer.SKY, above) >= 15
                            && isValidSpawnArea(level, candidate)) {
                        System.out.println("[DEBUG] true_end: Found ideal spawn: " + above);
                        return above;
                    }
                }
            }
        }
        return null;
    }

    public static BlockPos findFallbackSpawn(ServerLevel level, BlockPos centerPos) {
        int searchRadius = 32;
        for (int y = level.getMaxBuildHeight() - 16; y >= level.getMinBuildHeight() + 8; y--) {
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos candidate = centerPos.offset(x, y - centerPos.getY(), z);
                    BlockPos above = candidate.above();
                    if (level.getBlockState(candidate).is(TrueEndBlocks.GRASS_BLOCK.get())
                            && level.getBiome(candidate).is(ResourceLocation.parse("true_end:nostalgic_meadow"))
                            && isYInSpawnRange(level, candidate)
                            && noBadBlocks(level, candidate)
                            && level.isEmptyBlock(above)
                            && isValidSpawnArea(level, candidate)) {
                        System.out.println("[DEBUG] true_end: Found fallback spawn: " + above);
                        return above;
                    }
                }
            }
        }
        return null;
    }

    private static final int MAX_TERRAIN_DROP = 7;
    private static final int MAX_TERRAIN_ASCENT = 3;

    private static boolean isValidSpawnArea(ServerLevel level, BlockPos center) {
        int centerY = getLocalMax(level, new BlockPos(center.getX(), level.getMaxBuildHeight() - 1, center.getZ()));
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                BlockPos pos = new BlockPos(center.getX() + dx, level.getMaxBuildHeight() - 1, center.getZ() + dz);
                int terrainY = getLocalMax(level, pos);

                int deltaY = terrainY - centerY;
                if (deltaY > MAX_TERRAIN_ASCENT || -deltaY > MAX_TERRAIN_DROP) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean noBadBlocks(ServerLevel level, BlockPos center) {
        final int R = 3;
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        for (int dx = -R; dx <= R; dx++) {
            for (int dz = -R; dz <= R; dz++) {
                BlockPos atFeet = new BlockPos(cx + dx, cy, cz + dz);
                BlockPos below  = new BlockPos(cx + dx, cy - 1, cz + dz);
                BlockPos below2 = below.below();
                BlockState stateAtFeet = level.getBlockState(atFeet);
                BlockState stateBelow  = level.getBlockState(below);
                BlockState stateBelow2 = level.getBlockState(below2);

                //Return false if any of these are found in the area
                if (stateAtFeet.is(Blocks.WATER)
                        || stateBelow .is(Blocks.WATER)
                        || stateBelow2.is(Blocks.WATER)
                        || stateAtFeet.is(TrueEndBlocks.SAND.get())
                        || stateBelow .is(TrueEndBlocks.SAND.get())
                        || stateBelow2.is(TrueEndBlocks.SAND.get())) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean isYInSpawnRange(ServerLevel level, BlockPos pos) {
        int y = pos.getY();
        return y >= 66 && y <= 76;
    }


    private static void executeCommand(LevelAccessor world, Entity entity, String command) {
        if (world instanceof ServerLevel level && entity instanceof ServerPlayer player) {
            level.getServer().getCommands()
                    .performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), command);
        }
    }
    private static void sendFirstEntryConversation(ServerPlayer player, ServerLevel world) {
        int convoDelay = TrueEndVariables.btdConversationDelay.getValue();
        String[] conversation = {
                "[\"\",{\"text\":\"\\n\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\"? You've awakened.\",\"color\":\"dark_green\"},{\"text\":\"\\n\"}]"
                        .formatted(player.getName().getString()),
                "{\"text\":\"So soon, thought it'd dream longer...\",\"color\":\"dark_aqua\"}",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"Well, it's beyond the dream now. The player, \",\"color\":\"dark_green\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\", woke up.\",\"color\":\"dark_green\"}]"
                        .formatted(player.getName().getString()),
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"We left something for you in your home.\",\"color\":\"dark_aqua\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"Use it well.\",\"color\":\"dark_aqua\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"You may go back to the dream, a dream of a better world if you wish.\",\"color\":\"dark_green\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"We'll see you again soon, \",\"color\":\"dark_aqua\"},{\"selector\":\"%s\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_aqua\"},{\"text\":\"\\n\"}]"
                        .formatted(player.getName().getString())
        };
        TrueEnd.queueServerWork(45, () -> {
            TrueEnd.sendTellrawMessagesWithCooldown(player, conversation, convoDelay);
        });
    }

    public static void adaptTerrain(ServerLevel world, BlockPos centerPos) {
        BlockPos placePos = new BlockPos(centerPos.getX() - HOUSE_PLATEAU_WIDTH/2, centerPos.getY() - 1, centerPos.getZ() - HOUSE_PLATEAU_LENGTH/2);
        int plateauHeight = placePos.getY();
        // make the plateau
        for (int x = 0; x < HOUSE_PLATEAU_WIDTH; x++) {
            for (int z = 0; z < HOUSE_PLATEAU_LENGTH; z++) {
                BlockPos grassPos = new BlockPos(x + placePos.getX(), plateauHeight, z + placePos.getZ());
                placeGrassWithDirt(world, grassPos);
            }
        }
        int radius = TERRAIN_ADAPT_EXTENSION;
        int centerX = placePos.getX() + HOUSE_PLATEAU_WIDTH / 2;
        int centerZ = placePos.getZ() + HOUSE_PLATEAU_LENGTH / 2;
        int maxDist = radius + Math.max(HOUSE_PLATEAU_WIDTH, HOUSE_PLATEAU_LENGTH) / 2;
        // circular terrain adaptation
        for (int dx = -maxDist; dx <= maxDist; dx++) {
            for (int dz = -maxDist; dz <= maxDist; dz++) {
                int worldX = centerX + dx;
                int worldZ = centerZ + dz;

                double distFromCenter = Math.sqrt(dx * dx + dz * dz);
                if (distFromCenter > radius + (double) Math.max(HOUSE_PLATEAU_WIDTH, HOUSE_PLATEAU_LENGTH) / 2) continue;

                int localX = worldX - placePos.getX();
                int localZ = worldZ - placePos.getZ();
                boolean insidePlateau = localX >= 0 && localX < HOUSE_PLATEAU_WIDTH && localZ >= 0 && localZ < HOUSE_PLATEAU_LENGTH;
                if (insidePlateau) continue;

                BlockPos checkPos = new BlockPos(worldX, plateauHeight, worldZ);
                int targetHeight = getLocalMax(world, checkPos);

                int dist = (int) Math.round(distFromCenter) - Math.max(HOUSE_PLATEAU_WIDTH, HOUSE_PLATEAU_LENGTH) / 2;
                if (dist < 0 || dist > radius) continue;

                int height = gradient(targetHeight, plateauHeight, radius, dist);
                BlockPos grassPos = new BlockPos(worldX, height, worldZ);
                placeGrassWithDirt(world, grassPos);
            }
        }
    }
    // Helper method to place grass and fill with dirt until hitting stone
    private static void placeGrassWithDirt(ServerLevel world, BlockPos pos) {
        world.setBlock(pos, TrueEndBlocks.GRASS_BLOCK.get().defaultBlockState(), 3);
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for (int y = pos.getY() - 1; y >= world.getMinBuildHeight(); y--) {
            mutablePos.setY(y);
            BlockState current = world.getBlockState(mutablePos);
            if (current.is(Blocks.STONE)) {
                break;
            }
            world.setBlock(mutablePos, TrueEndBlocks.DIRT.get().defaultBlockState(), 3);
        }
    }
    // Smooth gradient function
    private static int gradient(int targetHeight, int centerHeight, int maxDist, int dist) {
        float t = (float) dist / maxDist;
        return Math.round(centerHeight * (1 - t) + targetHeight * t);
    }
    public static int getLocalMax(ServerLevel world, BlockPos pos) {
        int maxY = world.getMaxBuildHeight() - 1;
        int max = maxY;

        for (int y = maxY; y >= 0; y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.getBlockState(checkPos).getBlock() != Blocks.AIR &&
                    world.getBlockState(checkPos).getBlock() != TrueEndBlocks.WOOD.get() &&
                    world.getBlockState(checkPos).getBlock() != TrueEndBlocks.LEAVES.get()) {
                if (y < pos.getY()) {
                    return y - 1;
                } else {
                    // Above or at posY: remember it as potential max
                    max = y - 1;
                }
            }
        }
        return max;
    }

}