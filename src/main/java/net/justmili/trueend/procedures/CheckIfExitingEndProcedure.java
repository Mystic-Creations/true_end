package net.justmili.trueend.procedures;

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

import net.justmili.trueend.init.TrueEndModGameRules;
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

                        // Find a suitable spawn point on true_end:grass_block within Y 64-70 with sky check
                        BlockPos spawnPos = findGrassSpawnPoint(nextLevel, 64, 70);
                        if (spawnPos != null) {
                            serverPlayer.teleportTo(nextLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                            // Schedule the structure placement after a short delay
                            TrueEndMod.queueServerWork(20, () -> {
                                executeCommand(nextLevel, serverPlayer, "function true_end:build_home_structure");
                            });
                        } else {
                            // If no grass block is found, teleport to the default spawn
                            serverPlayer.teleportTo(nextLevel, nextLevel.getSharedSpawnPos().getX(), nextLevel.getSharedSpawnPos().getY(), nextLevel.getSharedSpawnPos().getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                            TrueEndMod.queueServerWork(20, () -> {
                                executeCommand(nextLevel, serverPlayer, "function true_end:build_home_structure");
                            });
                        }

                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                        for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                            serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                        serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                    }
                }
                if (world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.CLEAR_DREAM_ITEMS)) {
                    Player _player = (Player) entity;
                    _player.getInventory().clearContent();
                    world.getLevelData().getGameRules().getRule(TrueEndModGameRules.CLEAR_DREAM_ITEMS).set(false, world.getServer());

                    String[] convesation = {
                            "{\"text\":\" \"}",
                            "[\"\",{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\"? You've awakened.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
                            "{\"text\":\" \"}",
                            "{\"text\":\"So soon, thought it'd dream longer...\",\"color\":\"dark_aqua\"}",
                            "{\"text\":\" \"}",
                            "[\"\",{\"text\":\"Well, it's beyond the dream now. The player, \",\"color\":\"dark_green\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\", woke up.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
                            "{\"text\":\" \"}",
                            "{\"text\":\"We left something for you in your home.\",\"color\":\"dark_aqua\"}",
                            "{\"text\":\" \"}",
                            "{\"text\":\"Use it well.\",\"color\":\"dark_aqua\"}",
                            "{\"text\":\" \"}",
                            "{\"text\":\"You may go back to the dream, a dream of a better world if you wish.\",\"color\":\"dark_green\"}",
                            "{\"text\":\" \"}",
                            "[\"\",{\"text\":\"We'll see you again soon, \",\"color\":\"dark_aqua\"},{\"selector\":\"%s\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_aqua\"}]".formatted(player.getName().getString())
                    };

                    TrueEndMod.queueServerWork(44, () -> {
                        TrueEndMod.sendTellrawMessagesWithCooldown(player, convesation, world.getLevelData().getGameRules().getRule(TrueEndModGameRules.BTD_CONVERSATION_MESSEGE_DELAY).get());
                    });

                    world.getLevelData().getGameRules().getRule(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME).set(true, world.getServer());
                }
            }
        }
    }

    // Helper method to find a grass block within a Y range
    private static BlockPos findGrassSpawnPoint(ServerLevel level, int minY, int maxY) {
        ChunkPos chunkPos = new ChunkPos(level.getSharedSpawnPos());
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -16; x <= 16; x++) {
            for (int z = -16; z <= 16; z++) {
                for (int y = maxY; y >= minY; y--) {
                    mutablePos.set(chunkPos.getMinBlockX() + x, y, chunkPos.getMinBlockZ() + z);
                    if (level.getBlockState(mutablePos).is(net.justmili.trueend.init.TrueEndModBlocks.GRASS_BLOCK.get()) && isSkyClear(level, mutablePos)) {
                        return mutablePos.above();
                    }
                }
            }
        }
        return null; // Return null if no grass block is found
    }

    //Helper method to check if the sky is clear.
    private static boolean isSkyClear(ServerLevel level, BlockPos pos) {
        for (int i = 1; i <= 15; i++) {
            if (!level.getBlockState(pos.above(i)).isAir()) {
                return false;
            }
        }
        return true;
    }

    private static void executeCommand(LevelAccessor world, Entity entity, String command) {
        if (world instanceof ServerLevel _level && entity != null) {
            _level.getServer().getCommands().performPrefixedCommand(entity.createCommandSourceStack().withSuppressedOutput(), command);
        }
    }
}