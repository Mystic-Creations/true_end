package net.justmili.trueend.network.packets;

import net.justmili.trueend.command.calls.screentests.TestCredits;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowCreditsPacket {
    public ShowCreditsPacket() {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        TestCredits.execute();
    }

    public void toBytes(FriendlyByteBuf buf) {}
    public ShowCreditsPacket(FriendlyByteBuf buf) {}
}
