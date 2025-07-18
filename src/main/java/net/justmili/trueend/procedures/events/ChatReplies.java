package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChatReplies {

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        String msg = event.getMessage().getString().toLowerCase(Locale.ROOT).trim();
        LevelAccessor world = event.getPlayer().serverLevel();

        handleHardcodedReplies(world, msg);
        handleRandomReplies(world, msg);
    }

    public static void handleHardcodedReplies(LevelAccessor world, String msg) {
        switch (msg) {
            case "sleep" -> sendChatReply(world, "<§kUnknown§r> No more.");
            case "awake" -> sendChatReply(world, "<§kUnknown§r> You.");
            case "test" -> sendChatReply(world, "<§kUnknown§r> test");
            default -> {}
        }
    }

    public static void handleRandomReplies(LevelAccessor world, String msg) {}

    private static void sendChatReply(LevelAccessor world, String text) {
        if (!world.isClientSide() && world.getServer() != null) {
            int delay = (int) ((Math.random()*10)*5);
            TrueEnd.wait(delay, () -> {
                MinecraftServer server = world.getServer();
                server.getPlayerList().broadcastSystemMessage(Component.literal(text), false);
            });
        }
    }
}
