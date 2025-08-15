package net.justmili.trueend.network.packets;

import net.justmili.trueend.config.Config;
import net.justmili.trueend.network.Variables;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateClientConfig {
    private String key;
    private boolean value;

    public UpdateClientConfig() {}
    public UpdateClientConfig(String key, boolean value) {
        this.key = key;
        this.value = value;
    }
    public UpdateClientConfig(FriendlyByteBuf buf) {
        this.key = buf.readUtf(32767);
        this.value = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.key);
        buf.writeBoolean(this.value);
    }

    public static void handle(UpdateClientConfig msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Variables.fogToggle = msg.value;
            if (Config.entries != null) Config.entries.put(msg.key, msg.value);
        });
        ctx.setPacketHandled(true);
    }
}
