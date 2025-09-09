package net.mysticcreations.true_end.config;


import net.mysticcreations.true_end.TrueEnd;
import net.mysticcreations.true_end.init.Packets;
import net.mysticcreations.true_end.network.Variables;
import net.mysticcreations.true_end.network.packets.UpdateClientConfigPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ConfigSync {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        fogUpdate(event);
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        fogUpdate(event);
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        fogUpdate(event);
    }
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        fogUpdate(event);
    }

    private static void fogUpdate(PlayerEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {return;}
        TrueEnd.wait(5, () ->
                Packets.sendToPlayer(new UpdateClientConfigPacket("fogToggle", Variables.fogToggle), player)
        );
    }
}
