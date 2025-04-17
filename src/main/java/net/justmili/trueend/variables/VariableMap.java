package net.justmili.trueend.variables;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedData.Factory;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class VariableMap {

    // Stores global variables such as defaultKeepInv.
    public static class GlobalVariables extends SavedData {
        private static final String DATA_NAME = "trueend_global_vars";
        private boolean defaultKeepInv = false;

        public boolean isDefaultKeepInv() {
            return defaultKeepInv;
        }

        public void setDefaultKeepInv(boolean value) {
            this.defaultKeepInv = value;
            setDirty();
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("defaultKeepInv", defaultKeepInv);
            return tag;
        }

        public static GlobalVariables load(CompoundTag tag) {
            GlobalVariables data = new GlobalVariables();
            data.defaultKeepInv = tag.getBoolean("defaultKeepInv");
            return data;
        }

        /**
         * Retrieves the GlobalVariables instance for the given world.
         *
         * @param world The server level.
         * @return The GlobalVariables instance.
         */
        public static GlobalVariables get(ServerLevel world) {
            return world.getDataStorage().computeIfAbsent(GlobalVariables::load, GlobalVariables::new, DATA_NAME);
        }
    }

    // Stores per-player variables
    public static class PlayerVariables implements INBTSerializable<CompoundTag> {
        private boolean btdFirstTimeVisit = false;

        public boolean isBtdFirstTimeVisit() {
            return btdFirstTimeVisit;
        }

        public void setBtdFirstTimeVisit(boolean value) {
            this.btdFirstTimeVisit = value;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("btdFirstTimeVisit", btdFirstTimeVisit);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            btdFirstTimeVisit = tag.getBoolean("btdFirstTimeVisit");
        }
    }

    // Provides and manages the PlayerVariables capability.
    public static class PlayerVariablesProvider implements ICapabilitySerializable<CompoundTag> {
        public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
        private final PlayerVariables instance = new PlayerVariables();
        private final LazyOptional<PlayerVariables> optional = LazyOptional.of(() -> instance);

        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
            return cap == PLAYER_VARIABLES_CAPABILITY ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }
    // Handles attaching and cloning of the PlayerVariables capability.
    @Mod.EventBusSubscriber
    public static class PlayerVariablesEvents {

        private static final ResourceLocation PLAYER_VARIABLES_ID = new ResourceLocation("trueend", "player_variables");

        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<net.minecraft.world.entity.player.Player> event) {
            event.addCapability(PLAYER_VARIABLES_ID, new PlayerVariablesProvider());
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            if (!event.isWasDeath()) {
                return;
            }
            event.getOriginal().getCapability(PlayerVariablesProvider.PLAYER_VARIABLES_CAPABILITY).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerVariablesProvider.PLAYER_VARIABLES_CAPABILITY).ifPresent(newStore -> {
                    newStore.setBtdFirstTimeVisit(oldStore.isBtdFirstTimeVisit());
                });
            });
        }
    }
}
