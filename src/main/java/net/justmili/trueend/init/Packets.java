package net.justmili.trueend.init;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.packets.InvOpenAdvPacket;

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
    }

    public static void sendToServer(Object message) {
        TrueEnd.PACKET_HANDLER.sendToServer(message);
    }
}