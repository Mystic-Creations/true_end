package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.Variables;
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
import java.util.Random;

import static net.justmili.trueend.init.Dimensions.BTD;
import static net.justmili.trueend.procedures.randomevents.TimeChange.*;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChatReplies {
    private static final Random RNG = new Random();

    //Detection & Util
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        String msg = event.getMessage().getString().toLowerCase(Locale.ROOT).trim().replaceAll("[!?.-]+$", "");;
        LevelAccessor world = event.getPlayer().serverLevel();
        ServerPlayer player = event.getPlayer();

        hardcodedReplies(world, msg, player);
        randomReplies(world, msg, player);
    }
    private static void sendChatReply(LevelAccessor world, String text, Boolean cooldown) {
        int delay = 0;
        if (cooldown) delay = (int)((Math.random()*50)+25); //Hardcode reply delay
        if (!cooldown) delay = 15+(int)(Math.random()*46); //Random reply delay

        if (!world.isClientSide() && world.getServer() != null) {
            TrueEnd.wait(delay, () -> {
                MinecraftServer server = world.getServer();
                server.getPlayerList().broadcastSystemMessage(Component.literal(text), false);
            });
        }
    }

    //Main components
    public static void hardcodedReplies(LevelAccessor world, String msg, ServerPlayer player) {
        switch (msg) {
            case "sleep" -> sendChatReply(world, "<§kUnknown§r> No more.", true);
            case "awake" -> sendChatReply(world, "<§kUnknown§r> You.", true);
            case "where from" -> sendChatReply(world, "<§kUnknown§r> The fog.", true);
            case "why is it night" -> nightReply(player);
            case "is anyone there", "anyone there" -> sendChatReply(world, "<§kUnknown§r> Yes.", true);
            case "who are you" -> sendChatReply(world, "<§kUnknown§r> Unknown. Forgotten.", true);
            case "the broken script" -> sendChatReply(world, "<§kUnknown§r> Inspiration.", true);
            case "nightmare" -> sendChatReply(world, "<§kUnknown§r> Within", true);
            case "fuck you" -> punish(player);
            default -> {}
        }
    }
    public static void randomReplies(LevelAccessor world, String msg, ServerPlayer player) {
        if (player.level().dimension() == BTD) return;
        if (!(Math.random() < (Variables.randomEventChance)/48)) return;
        String[] messages = {
                "<§kUnknown§r> This isn't real.",
                "<§kUnknown§r> Wake up.",
                "<§kUnknown§r> "+player.getName().getString().trim()+".",
                "<§kUnknown§r> They see you.",
                "<§kUnknown§r> The world changes, but not them."
        };
        String chat = messages[RNG.nextInt(messages.length)];
        sendChatReply(world, chat, false);
    }

    //More behavior
    public static void nightReply(ServerPlayer player) {
        ServerLevel world = (ServerLevel) player.level();
        long time = world.getDayTime() % 24000;
        boolean isDay = time >= DAY && time < NIGHT;
        if (!isDay) {
            sendChatReply(world, "<§kUnknown§r> Sleep.", true);
        } else {
            sendChatReply(world, "<§kUnknown§r> It is not.", true);
        }
    }
    public static void punish(ServerPlayer player) {
        int delay = (int) ((Math.random()*50)+20);
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
