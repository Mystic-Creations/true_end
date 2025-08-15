package net.justmili.trueend.init;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.packets.InvOpenAdvPacket;
import net.justmili.trueend.network.packets.UpdateClientConfigPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

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
    }

    public static void sendToServer(Object message) {
        TrueEnd.PACKET_HANDLER.sendToServer(message);
    }
    public static void sendToPlayer(Object message, ServerPlayer player) {
        TrueEnd.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}