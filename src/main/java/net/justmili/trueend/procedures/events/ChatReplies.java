package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;

import static net.justmili.trueend.procedures.randomevents.TimeChange.*;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChatReplies {

    //Detection & Util
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        String msg = event.getMessage().getString().toLowerCase(Locale.ROOT).trim().replaceAll("[!?.-]+$", "");;
        LevelAccessor world = event.getPlayer().serverLevel();
        ServerPlayer player = event.getPlayer();

        hardcodedReplies(world, msg, player);
        randomReplies(world, msg);
    }
    private static void sendChatReply(LevelAccessor world, String text) {
        if (!world.isClientSide() && world.getServer() != null) {
            int delay = (int) (((Math.random()*10)*5)+25);
            TrueEnd.wait(delay, () -> {
                MinecraftServer server = world.getServer();
                server.getPlayerList().broadcastSystemMessage(Component.literal(text), false);
            });
        }
    }

    //Main components
    public static void hardcodedReplies(LevelAccessor world, String msg, ServerPlayer player) {
        switch (msg) {
            case "sleep" -> sendChatReply(world, "<§kUnknown§r> No more.");
            case "awake" -> sendChatReply(world, "<§kUnknown§r> You.");
            case "where from" -> sendChatReply(world, "<§kUnknown§r> The fog.");
            case "why is it night" -> nightReply(player);
            case "is anyone there", "anyone there" -> sendChatReply(world, "<§kUnknown§r> Yes.");
            case "who are you" -> sendChatReply(world, "<§kUnknown§r> Unknown. Forgotten.");
            case "the broken script" -> sendChatReply(world, "<§kUnknown§r> Inspiration.");
            case "nightmare" -> sendChatReply(world, "<§kUnknown§r> Within");
            case "fuck you" -> punish(player);
            default -> {}
        }
    }
    public static void randomReplies(LevelAccessor world, String msg) {}

    //More behavior
    public static void nightReply(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();
        long time = world.getDayTime() % 24000;
        boolean isDay = time >= DAY && time < NIGHT;
        if (!isDay) {
            sendChatReply(world, "<§kUnknown§r> Sleep.");
        } else {
            sendChatReply(world, "<§kUnknown§r> It is not.");
        }
    }
    public static void punish(ServerPlayer player) {
        int delay = (int) (((Math.random()*10)*5)+20);
        TrueEnd.wait(delay, () -> {
            player.setGameMode(GameType.ADVENTURE);
            MinecraftServer server = player.server;
            String playerName = player.getDisplayName().getString();
            String textA = "§7§o["+playerName+"'s game mode has been changed to Adventure Mode by §kUnknown§r§7§o]";
            server.getPlayerList().broadcastSystemMessage(Component.literal(textA), false);
            TrueEnd.wait(6000, () -> {
                player.setGameMode(GameType.SURVIVAL);
                String textS = "§7§o["+playerName+"'s game mode has been changed to Survival Mode by §kUnknown§r§7§o]";
                server.getPlayerList().broadcastSystemMessage(Component.literal(textS), false);
            });
        });
    }
}
