package net.mysticcreations.true_end.procedures.events;

import net.mysticcreations.true_end.config.Config;
import net.mysticcreations.true_end.init.Packets;
import net.mysticcreations.true_end.network.Variables;
import net.mysticcreations.true_end.network.packets.ShowCreditsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.mysticcreations.true_end.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class PlayCredits {
    private static boolean hasShownCreditsThisSession = false;
    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (Variables.creditsToggle) { hasShownCreditsThisSession = false; } else { return; }
        if (hasShownCreditsThisSession) return;

        if (event.getFrom() == BTD && event.getTo() == Level.OVERWORLD) {
            hasShownCreditsThisSession = true;
            Config.updateConfig("creditsToggle", false);

            ServerPlayer player = (ServerPlayer) event.getEntity();
            Packets.sendToPlayer(new ShowCreditsPacket(), player);
        }
    }
}