package net.justmili.trueend.config;


import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Packets;
import net.justmili.trueend.network.Variables;
import net.justmili.trueend.network.packets.UpdateClientConfigPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ConfigSync {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {return;}
        TrueEnd.wait(5, () ->
        Packets.sendToPlayer(new UpdateClientConfigPacket("fogToggle", Variables.fogToggle), player)
        );
    }

}
