package net.justmili.trueend.procedures;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.AdvancementEvent;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.LightLayer;

import net.justmili.trueend.init.TrueEndModGameRules;
import net.justmili.trueend.init.TrueEndModBlocks;
import net.justmili.trueend.TrueEndMod;

import javax.annotation.Nullable;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CheckIfExitingEndProcedure {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        execute(event, event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        if (!world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME)) {
            if ((world instanceof Level level ? level.dimension() : (world instanceof WorldGenLevel worldGenLevel ? worldGenLevel.getLevel().dimension() : Level.OVERWORLD)) == Level.OVERWORLD && entity instanceof ServerPlayer player
                    && player.level() instanceof ServerLevel && player.getAdvancements().getOrStartProgress(Objects.requireNonNull(player.server.getAdvancements().getAdvancement(ResourceLocation.parse("true_end:stop_dreaming")))).isDone()) {
                if (entity instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {
                    ResourceKey<Level> destinationType = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("true_end:beyond_the_dream"));
                    if (serverPlayer.level().dimension() == destinationType)
                        return;
                    ServerLevel nextLevel = serverPlayer.server.getLevel(destinationType);
                    if (nextLevel != null) {
                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

                        BlockPos biomePos = TrueEndMod.locateBiome(nextLevel, serverPlayer.blockPosition(), "true_end:nostalgic_meadow");
                        BlockPos spawnPos;
                        if (biomePos == null) {
                            spawnPos = null;
                        } else {
                            player.sendSystemMessage(Component.literal("biomePos = X: %d Y: %d Z: %d".formatted(biomePos.getX(), biomePos.getY(), biomePos.getZ())));
                            // Find a suitable spawn point with all conditions
                            spawnPos = findIdealSpawnPoint(nextLevel, biomePos);
                        }
                        if (spawnPos != null) {
                            serverPlayer.teleportTo(nextLevel, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot()); //Teleport to center of block
                            // Schedule the structure placement after a short delay
                            TrueEndMod.queueServerWork(10, () -> {
                                executeCommand(nextLevel, serverPlayer, "function true_end:build_home_structure");
                            });

                            serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                            for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                            serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                        } else {
                            //fallback teleport if no valid position is found.
                            if (biomePos == null) {
                                int y = 129;
                                boolean foundPlace = false;

                                while (y > 0) {
                                    BlockPos pos = new BlockPos(serverPlayer.getBlockX(), y, serverPlayer.getBlockZ());
                                    BlockPos posAbove = pos.above();
                                    BlockPos posBelow = pos.below();

                                    boolean isEmpty = nextLevel.isEmptyBlock(pos);
                                    boolean isAboveEmpty = nextLevel.isEmptyBlock(posAbove);
                                    boolean isBelowSolid = !nextLevel.isEmptyBlock(posBelow);

                                    if (isEmpty && isAboveEmpty && isBelowSolid) {
                                        foundPlace = true;
                                        break;
                                    }
                                    y--;
                                }

                                // If no valid spot is found, set a fallback
                                if (!foundPlace) {
                                    y = 129;
                                }
                                serverPlayer.teleportTo(nextLevel,serverPlayer.getX(), y, serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                                TrueEndMod.queueServerWork(10, () -> {
                                    executeCommand(nextLevel, serverPlayer, "function true_end:build_home_structure");
                                });

                                serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                                for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                    serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                                serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                            } else {
                                int y = 129;
                                boolean foundPlace = false;

                                while (y > 0) {
                                    BlockPos pos = new BlockPos(serverPlayer.getBlockX(), y, serverPlayer.getBlockZ());
                                    BlockPos posAbove = pos.above();
                                    BlockPos posBelow = pos.below();

                                    boolean isEmpty = nextLevel.getBlockState(pos).is(Blocks.AIR);
                                    boolean isAboveEmpty = nextLevel.getBlockState(posAbove).is(Blocks.AIR);
                                    boolean isBelowSolid = !nextLevel.getBlockState(posBelow).is(Blocks.AIR);

                                    if (isEmpty && isAboveEmpty && isBelowSolid) {
                                        foundPlace = true;
                                        break;
                                    }
                                    y--;
                                }

                                // If no valid spot is found, set a fallback
                                if (!foundPlace) {
                                    y = 129;
                                }
                                serverPlayer.teleportTo(nextLevel,biomePos.getX(), y, biomePos.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                                TrueEndMod.queueServerWork(10, () -> {
                                    executeCommand(nextLevel, serverPlayer, "function true_end:build_home_structure");
                                });

                                serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                                for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                    serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                                serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                            }
                        }
                    }
                }
                if (world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.CLEAR_DREAM_ITEMS)) {
                    Player _player = (Player) entity;
                    _player.getInventory().clearContent();
                    world.getLevelData().getGameRules().getRule(TrueEndModGameRules.CLEAR_DREAM_ITEMS).set(false, world.getServer());

                    String[] convesation = {
                            "[\"\",{\"text\":\"\\n\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\"? You've awakened.\",\"color\":\"dark_green\"},{\"text\":\"\\n\"}]".formatted(player.getName().getString()),
                            "{\"text\":\"So soon, thought it'd dream longer...\",\"color\":\"dark_aqua\"}",
                            "[\"\",{\"text\":\"\\n\"},{\"text\":\"Well, it's beyond the dream now. The player, \",\"color\":\"dark_green\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\", woke up.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
                            "[\"\",{\"text\":\"\\n\"},{\"text\":\"We left something for you in your home.\",\"color\":\"dark_aqua\"}]",
                            "[\"\",{\"text\":\"\\n\"},{\"text\":\"Use it well.\",\"color\":\"dark_aqua\"}]",
                            "[\"\",{\"text\":\"\\n\"},{\"text\":\"You may go back to the dream, a dream of a better world if you wish.\",\"color\":\"dark_green\"}]",
                            "[\"\",{\"text\":\"\\n\"},{\"text\":\"We'll see you again soon, \",\"color\":\"dark_aqua\"},{\"selector\":\"%s\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_aqua\"},{\"text\":\"\\n\"}]".formatted(player.getName().getString())
                    };

                    TrueEndMod.queueServerWork(44, () -> {
                        TrueEndMod.sendTellrawMessagesWithCooldown(player, convesation, world.getLevelData().getGameRules().getRule(TrueEndModGameRules.BTD_CONVERSATION_MESSEGE_DELAY).get());
                    });

                    world.getLevelData().getGameRules().getRule(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME).set(true, world.getServer());
                }
            }
        }
    }

    public static BlockPos findIdealSpawnPoint(ServerLevel level, BlockPos blockPos) {
        int playerX = blockPos.getX();
        int playerZ = blockPos.getZ();

        for (int xOffset = -6; xOffset <= 6; xOffset++) {
            for (int zOffset = -6; zOffset <= 6; zOffset++) {
                for (int y = 85; y >= 65; y--) {
                    BlockPos pos = new BlockPos(playerX + xOffset, y, playerZ + zOffset);
    
                    // ensure biome and grass block conditions are met
                    if (level.getBiome(pos).is(ResourceLocation.parse("true_end:nostalgic_meadow")) &&
                        level.getBlockState(pos.below()).is(TrueEndModBlocks.GRASS_BLOCK.get())) {

                        // light level and flat area condition check
                        if (level.getBrightness(LightLayer.SKY, pos) >= 15 && isFlatArea(pos)) {
                            return pos; // Found a suitable spawn point
                        }
                    }
                }
            }
        }
        return null; // No suitable spawn point found
    }

    // Helper method to check for a flat area (6x6)
    private static boolean isFlatArea(BlockPos pos) {
        int unflatness = 0;
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (pos.below().getY() != pos.offset(x, 0, z).below().getY()) {
                    unflatness++;
                }
            }
        }
        return unflatness < 7;
    }

    private static void executeCommand(LevelAccessor world, Entity entity, String command) {
        if (world instanceof ServerLevel _level && entity != null) {
            _level.getServer().getCommands().performPrefixedCommand(entity.createCommandSourceStack().withSuppressedOutput(), command);
        }
    }
}