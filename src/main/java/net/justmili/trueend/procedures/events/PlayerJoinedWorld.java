package net.justmili.trueend.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

@Mod.EventBusSubscriber
public class PlayerJoinedWorld {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        execute(event);
    }

    private static void execute(PlayerEvent.PlayerLoggedInEvent event) {
        if (event == null) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        // Use getCommandSenderWorld() to get the server-level world.
        ServerLevel world = (ServerLevel) player.getCommandSenderWorld();
        if (world.getServer().getPlayerList().getPlayerCount() == 1) {
            String message = Component.translatable("multiplayer.player.joined").getString();
            message = String.format(message, player.getName().getString());
            world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(message).withStyle(ChatFormatting.YELLOW), false);
        }
    }
}
