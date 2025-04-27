package net.justmili.trueend.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Supplier;
import net.justmili.trueend.TrueEndMod;

/**
 * Per-player variables for the True End mod.
 */
public static class TrueEndVariables.PlayerVariables {
    private boolean beenBeyond = false;

    public boolean hasBeenBeyond() {
        return beenBeyond;
    }

    public void setBeenBeyond(boolean value) {
        this.beenBeyond = value;
    }

    /**
     * Sync this player's variables to the client.
     */
    public void syncPlayerVariables(Entity entity) {
        if (entity instanceof ServerPlayer server) {
            TrueEndMod.PACKET_HANDLER.send(
                PacketDistributor.PLAYER.with(() -> server),
                new TrueEndVariables.PlayerVariablesSyncMessage(this)
            );
        }
    }

    /**
     * Serialize to NBT.
     */
    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("beenBeyond", beenBeyond);
        return tag;
    }

    /**
     * Deserialize from NBT.
     */
    public void readNBT(Tag nbt) {
        CompoundTag tag = (CompoundTag) nbt;
        this.beenBeyond = tag.getBoolean("beenBeyond");
    }
}