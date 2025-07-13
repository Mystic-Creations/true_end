package net.justmili.trueend.network;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.packets.GrantInventoryAdvancementPackets;

public class TrueEndModMessages {
    private static int id = 0;

    public static void register() {
        TrueEnd.PACKET_HANDLER.registerMessage(
            id++,
            GrantInventoryAdvancementPackets.class,
            GrantInventoryAdvancementPackets::toBytes,
            GrantInventoryAdvancementPackets::new,
            GrantInventoryAdvancementPackets::handle
        );
    }

    public static void sendToServer(Object message) {
        TrueEnd.PACKET_HANDLER.sendToServer(message);
    }
}