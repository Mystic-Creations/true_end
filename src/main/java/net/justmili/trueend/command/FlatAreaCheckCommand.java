package net.justmili.trueend.command;

import net.justmili.trueend.TrueEndMod;
import net.justmili.trueend.procedures.CheckIfExitingEndProcedure;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FlatAreaCheckCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("flatterraincheck")
                .requires(s -> s.hasPermission(4))
                .executes(arguments -> {
                    Entity entity = arguments.getSource().getEntity();
                    if (entity != null) {
                        ServerLevel world = (ServerLevel) entity.level();
                        BlockPos center = entity.blockPosition();

                        // Locate the nearest biome

                        BlockPos biomePos;

                        biomePos = TrueEndMod.locateBiome(world, center, "true_end:nostalgic_meadow");
                        if (biomePos != null) {
                            // Find the best spawn position
                            BlockPos spawnPos = CheckIfExitingEndProcedure.findIdealSpawnPoint(world, biomePos);
                            if (spawnPos != null) {
                                entity.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                                entity.sendSystemMessage(Component.literal("Teleported to: X: %d Y: %d Z: %d"
                                        .formatted(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ())));
                            } else {
                                entity.sendSystemMessage(Component.literal("No suitable spawn location found in the biome."));
                            }
                        } else {
                            entity.sendSystemMessage(Component.literal("No nostalgic meadow biome found nearby."));
                        }
                    }
                    return 0;
                }));
    }
}