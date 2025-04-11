package net.justmili.trueend.procedures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.justmili.trueend.init.TrueEndModGameRules;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.justmili.trueend.init.TrueEndModBlocks;
import net.justmili.trueend.TrueEndMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ChunkPos;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CheckIfExitingEnd {
    private static final Map<ServerPlayer, Boolean> HAS_PROCESSED = new HashMap<>();

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (event.getAdvancement().getId().equals(ResourceLocation.parse("true_end:stop_dreaming"))) {
            execute(event, event.getEntity().level(), event.getEntity());
        }
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (!(entity instanceof ServerPlayer serverPlayer))
            return;

        if (HAS_PROCESSED.containsKey(serverPlayer) && HAS_PROCESSED.get(serverPlayer)) {
            return;
        }

        if (!world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME)) {
            if (serverPlayer.level().dimension() == Level.OVERWORLD
                    && serverPlayer.level() instanceof ServerLevel
                    && serverPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("true_end:stop_dreaming")))).isDone()) {

                HAS_PROCESSED.put(serverPlayer, true);

                ResourceKey<Level> destinationType = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:beyond_the_dream"));
                ServerLevel nextLevel = serverPlayer.server.getLevel(destinationType);
                if (nextLevel == null || serverPlayer.level().dimension() == destinationType) {
                    HAS_PROCESSED.remove(serverPlayer);
                    return;
                }

                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

                // Get the chunk the player is leaving
                ChunkPos leavingChunkPos = serverPlayer.level().getChunkAt(serverPlayer.blockPosition()).getPos();

                // Switch dimension
                //serverPlayer.changeDimension(nextLevel);

                // Explicitly unload the chunk in the Overworld (after player leaves)
                TrueEndMod.queueServerWork(10, () -> {
                    if (world instanceof ServerLevel overworldFinal) {
                        int chunkX = leavingChunkPos.x;
                        int chunkZ = leavingChunkPos.z;
                        overworldFinal.unload(overworldFinal.getChunk(chunkX, chunkZ));
                    }
                });

                // After dimension change, find a suitable spawn
                TrueEndMod.queueServerWork(15, () -> {
                    BlockPos initialSearchPos = TrueEndMod.locateBiome(nextLevel, serverPlayer.blockPosition(), "true_end:nostalgic_meadow");
                    if (initialSearchPos == null) {
                        initialSearchPos = serverPlayer.blockPosition();
                    }
                    BlockPos spawnPos = findIdealSpawnPoint(nextLevel, initialSearchPos);

                    if (spawnPos != null) {
                        serverPlayer.teleportTo(nextLevel, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                        for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                            serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                        serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                        // Delay other actions slightly
                        TrueEndMod.queueServerWork(30, () -> {
                            executeCommand(nextLevel, serverPlayer, "function true_end:build_home");
                            sendFirstEntryConversation(serverPlayer, nextLevel);
                            nextLevel.getGameRules().getRule(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME).set(true, nextLevel.getServer());
                            HAS_PROCESSED.remove(serverPlayer);

                            // Potential fix: Delay entity spawning by a few ticks
                            TrueEndMod.queueServerWork(60, () -> { // Delay for 3 seconds (20 ticks per second)
                                // No explicit entity spawning here, relying on natural spawn
                                System.out.println("Delaying natural entity spawning in BTD.");
                            });
                        });

                    } else {
                        // Enhanced fallback spawn logic
                        BlockPos fallbackSpawn = findFallbackSpawn(nextLevel, initialSearchPos);
                        if (fallbackSpawn != null) {
                            serverPlayer.teleportTo(nextLevel, fallbackSpawn.getX() + 0.5, fallbackSpawn.getY(), fallbackSpawn.getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
                            serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                            for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                            serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));

                            // Delay other actions slightly
                            TrueEndMod.queueServerWork(30, () -> {
                                executeCommand(nextLevel, serverPlayer, "function true_end:build_home");
                                sendFirstEntryConversation(serverPlayer, nextLevel);
                                nextLevel.getGameRules().getRule(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME).set(true, nextLevel.getServer());
                                HAS_PROCESSED.remove(serverPlayer);

                                // Potential fix: Delay entity spawning by a few ticks
                                TrueEndMod.queueServerWork(60, () -> { // Delay for 3 seconds
                                    // No explicit entity spawning here, relying on natural spawn
                                    System.out.println("Delaying natural entity spawning in BTD (fallback).");
                                });
                            });
                        } else {
                            System.out.println("SEVERE: Could not find ANY fallback spawn point!");
                            BlockPos worldSpawn = nextLevel.getSharedSpawnPos();
                            serverPlayer.teleportTo(nextLevel, worldSpawn.getX() + 0.5, worldSpawn.getY(), worldSpawn.getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
                            HAS_PROCESSED.remove(serverPlayer);
                        }
                    }

                    // Clear inventory if the gamerule is true
                    if (nextLevel.getGameRules().getBoolean(TrueEndModGameRules.CLEAR_DREAM_ITEMS)) {
                        Player _player = serverPlayer;
                        _player.getInventory().clearContent();
                        nextLevel.getGameRules().getRule(TrueEndModGameRules.CLEAR_DREAM_ITEMS).set(false, nextLevel.getServer());
                    }
                });
            }
        }
    }

    public static BlockPos findIdealSpawnPoint(ServerLevel level, BlockPos centerPos) {
        int searchRadius = 16; // Check a reasonable area
        for (int y = 85; y >= 65; y--) { // Iterate through potential ground levels
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos groundPos = centerPos.offset(x, y - centerPos.getY(), z);
                    BlockPos abovePos = groundPos.above();
                    BlockPos aboveAbovePos = abovePos.above();

                    if (level.getBlockState(groundPos).is(TrueEndModBlocks.GRASS_BLOCK.get()) &&
                        level.getBiome(groundPos).is(ResourceLocation.parse("true_end:nostalgic_meadow")) &&
                        level.isEmptyBlock(abovePos) &&
                        level.isEmptyBlock(aboveAbovePos) &&
                        level.getBrightness(LightLayer.SKY, abovePos) >= 15 &&
                        isFlatArea(level, groundPos)) {
                        System.out.println("Found ideal spawn: " + abovePos);
                        return abovePos;
                    }
                }
            }
        }
        System.out.println("No suitable ideal spawn point found within the search radius.");
        return null;
    }

    public static BlockPos findFallbackSpawn(ServerLevel level, BlockPos centerPos) {
        int searchRadius = 32; // Wider search for fallback
        for (int y = level.getMaxBuildHeight() - 5; y >= level.getMinBuildHeight() + 5; y--) {
            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int z = -searchRadius; z <= searchRadius; z++) {
                    BlockPos groundPos = centerPos.offset(x, y - centerPos.getY(), z);
                    BlockPos abovePos = groundPos.above();

                    if (level.getBlockState(groundPos).is(TrueEndModBlocks.GRASS_BLOCK.get()) &&
                        level.getBiome(groundPos).is(ResourceLocation.parse("true_end:nostalgic_meadow")) &&
                        level.isEmptyBlock(abovePos)) {
                        System.out.println("Found fallback spawn: " + abovePos);
                        return abovePos;
                    }
                }
            }
        }
        System.out.println("No fallback spawn point found.");
        return null;
    }

    // Helper method to check for a flat area (6x6) with solid ground below
    private static boolean isFlatArea(Level level, BlockPos pos) {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                BlockPos belowPos = pos.offset(x, -1, z);
                if (pos.getY() != belowPos.getY() + 1) { // Check if the block below is at y-1
                    return false;
                }
                if (level.getBlockState(belowPos).isAir()) {
                    return false; // Check if the block below is air
                }
            }
        }
        return true;
    }

    private static void executeCommand(LevelAccessor world, Entity entity, String command) {
        if (world instanceof ServerLevel _level && entity instanceof ServerPlayer _player) {
            _level.getServer().getCommands().performPrefixedCommand(_player.createCommandSourceStack().withSuppressedOutput(), command);
        }
    }

    private static void sendFirstEntryConversation(ServerPlayer player, ServerLevel world) {
        String[] conversation = {
                "[\"\",{\"text\":\"\\n\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\"? You've awakened.\",\"color\":\"dark_green\"},{\"text\":\"\\n\"}]".formatted(player.getName().getString()),
                "{\"text\":\"So soon, thought it'd dream longer...\",\"color\":\"dark_aqua\"}",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"Well, it's beyond the dream now. The player, \",\"color\":\"dark_green\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\", woke up.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"We left something for you in your home.\",\"color\":\"dark_aqua\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"Use it well.\",\"color\":\"dark_aqua\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"You may go back to the dream, a dream of a better world if you wish.\",\"color\":\"dark_green\"}]",
                "[\"\",{\"text\":\"\\n\"},{\"text\":\"We'll see you again soon, \",\"color\":\"dark_aqua\"},{\"selector\":\"%s\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_aqua\"},{\"text\":\"\\n\"}]".formatted(player.getName().getString())
        };
        TrueEndMod.queueServerWork(44, () -> {
            TrueEndMod.sendTellrawMessagesWithCooldown(player, conversation, world.getGameRules().getRule(TrueEndModGameRules.BTD_CONVERSATION_MESSEGE_DELAY).get());
        });
    }
}