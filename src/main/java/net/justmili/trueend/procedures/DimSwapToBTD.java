package net.justmili.trueend.procedures;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;
import static net.justmili.trueend.regs.IntegerRegistry.*;

@Mod.EventBusSubscriber
public class DimSwapToBTD {
    private static final Map<ServerPlayer, Boolean> HAS_PROCESSED = new HashMap<>();

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
                    String[] searchBiomes = { "true_end:nostalgic_meadow", "true_end:dreamers_beach" };
                    BlockPos initialSearchPos = TrueEnd.locateBiomes(nextLevel, worldSpawn, searchBiomes);
                    if (initialSearchPos == null)
                        initialSearchPos = worldSpawn;

                    BlockPos spawnPos = findIdealSpawnPoint(nextLevel, initialSearchPos);

                    BlockPos secondarySearchPos = TrueEnd.locateBiomes(nextLevel,
                            new BlockPos(new Vec3i(BlockPosRandomX, BlockPosRandomY, BlockPosRandomZ)), searchBiomes);

                    if (spawnPos == null) {
                        while (spawnPos == null) {
                            secondarySearchPos = TrueEnd.locateBiomes(nextLevel,
                                    new BlockPos(new Vec3i(BlockPosRandomX + BlockPosRandomZ, BlockPosRandomY,
                                            BlockPosRandomZ + BlockPosRandomX)),
                                    searchBiomes);

                            spawnPos = findFallbackSpawn(nextLevel, secondarySearchPos);
                        }
                    }

                    if (spawnPos == null) {
                        System.out.println("TRUE_END: Could not find ANY fallback spawn point!");
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
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockState state = level.getBlockState(mutablePos);
                    Block block = state.getBlock();

                    // Check if it's a log or leaves using Minecraft's built-in tags
                    if (block.defaultBlockState().is(BlockTags.LEAVES)) {
                        level.removeBlock(mutablePos, false);
                    }
                }
            }
        }
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
                            && level.isEmptyBlock(above)
                            && level.isEmptyBlock(above2)
                            && level.getBrightness(LightLayer.SKY, above) >= 15
                            && isValidSpawnArea(level, candidate)) {
                        System.out.println("TRUE_END: Found ideal spawn: " + above);
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
                            && level.isEmptyBlock(above)
                            && isValidSpawnArea(level, candidate)) {
                        System.out.println("TRUE_END: Found fallback spawn: " + above);
                        return above;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isValidSpawnArea(ServerLevel level, BlockPos center) {
        int grassCount = 0;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                if (level.getBlockState(pos).is(TrueEndBlocks.GRASS_BLOCK.get()))
                    grassCount++;
                if (level.getBlockState(pos).is(Blocks.WATER)
                        || level.getBlockState(pos.below()).is(Blocks.WATER)
                        || level.getBlockState(pos.below(2)).is(Blocks.WATER)) {
                    return false;
                }
            }
        }
        return grassCount >= 80;
    }

    private static void executeCommand(LevelAccessor world, Entity entity, String command) {
        if (world instanceof ServerLevel level && entity instanceof ServerPlayer player) {
            level.getServer().getCommands()
                    .performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), command);
        }
    }

    private static void sendFirstEntryConversation(ServerPlayer player, ServerLevel world) {
        int convoDelay = (int) TrueEndVariables.MapVariables.get(world).getBtdConversationDelay();
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
}
