package net.mysticcreations.true_end.init;

import net.mysticcreations.true_end.TrueEnd;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.mysticcreations.true_end.network.packets.*;

public class Packets {
    private static int id = 0;

    public static void register() {
        TrueEnd.PACKET_HANDLER.registerMessage(
            id++,
            InvOpenAdvPacket.class,
            InvOpenAdvPacket::toBytes,
            InvOpenAdvPacket::new,
            InvOpenAdvPacket::handle
        );
        TrueEnd.PACKET_HANDLER.registerMessage(
            id++,
            UpdateClientConfigPacket.class,
            UpdateClientConfigPacket::toBytes,
            UpdateClientConfigPacket::new,
            UpdateClientConfigPacket::handle
        );
        TrueEnd.PACKET_HANDLER.registerMessage(
            id++,
            ShowCreditsPacket.class,
            ShowCreditsPacket::toBytes,
            ShowCreditsPacket::new,
            ShowCreditsPacket::handle
        );
    }

    public static void sendToServer(Object message) {
        TrueEnd.PACKET_HANDLER.sendToServer(message);
    }
    public static void sendToPlayer(Object message, ServerPlayer player) {
        TrueEnd.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}