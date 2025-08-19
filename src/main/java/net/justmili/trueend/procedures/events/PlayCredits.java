package net.justmili.trueend.procedures.events;

import net.justmili.trueend.config.Config;
import net.justmili.trueend.init.Packets;
import net.justmili.trueend.network.Variables;
import net.justmili.trueend.network.packets.ShowCreditsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.init.Dimensions.BTD;

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