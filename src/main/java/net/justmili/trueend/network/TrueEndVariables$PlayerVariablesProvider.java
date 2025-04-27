package net.justmili.trueend.network;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Provides the PlayerVariables capability on each player.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrueEndVariables$PlayerVariablesProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation(TrueEndMod.MODID, "player_variables");

    private final TrueEndVariables.PlayerVariables variables = new TrueEndVariables.PlayerVariables();
    private final LazyOptional<TrueEndVariables.PlayerVariables> optional = LazyOptional.of(() -> variables);

    /**
     * Attach the capability to each non-fake Player entity.
     */
    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(ID, new TrueEndVariables$PlayerVariablesProvider());
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == TrueEndVariables.PLAYER_VARS_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return variables.writeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        variables.readNBT(nbt);
    }
}