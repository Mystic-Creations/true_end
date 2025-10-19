package net.mysticcreations.true_end.network.packets;

import net.mysticcreations.true_end.network.Variables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateClientConfigPacket {
    private String key;
    private boolean value;

    public UpdateClientConfigPacket() {}
    public UpdateClientConfigPacket(String key, boolean value) {
        this.key = key;
        this.value = value;
    }
    public UpdateClientConfigPacket(FriendlyByteBuf buf) {
        this.key = buf.readUtf(32767);
        this.value = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.key);
        buf.writeBoolean(this.value);
    }

    public static void handle(UpdateClientConfigPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> Variables.showFogClient = msg.value);
        ctx.setPacketHandled(true);
    }
}
