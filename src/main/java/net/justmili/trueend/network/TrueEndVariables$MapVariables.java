package net.justmili.trueend.network;

import net.justmili.trueend.TrueEndMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;

public class TrueEndVariables.MapVariables extends SavedData {
    public static final String DATA_NAME = "true_end_mapvars";

    private boolean defaultKeepInv = false;
    private boolean clearDreamItems = true;
    private double btdSpawnX = 0.0;
    private double btdSpawnY = 64.0;
    private double btdSpawnZ = 0.0;

    public boolean isDefaultKeepInv() { return defaultKeepInv; }
    public boolean isClearDreamItems() { return clearDreamItems; }
    public double getBtdSpawnX() { return btdSpawnX; }
    public double getBtdSpawnY() { return btdSpawnY; }
    public double getBtdSpawnZ() { return btdSpawnZ; }

    public void setDefaultKeepInv(boolean v) { defaultKeepInv = v; setDirty(); }
    public void setClearDreamItems(boolean v) { clearDreamItems = v; setDirty(); }
    public void setBtdSpawnX(double v) { btdSpawnX = v; setDirty(); }
    public void setBtdSpawnY(double v) { btdSpawnY = v; setDirty(); }
    public void setBtdSpawnZ(double v) { btdSpawnZ = v; setDirty(); }

    public static MapVariables load(CompoundTag nbt) {
        MapVariables m = new MapVariables();
        m.defaultKeepInv = nbt.getBoolean("defaultKeepInv");
        m.clearDreamItems = nbt.getBoolean("clearDreamItems");
        m.btdSpawnX = nbt.getDouble("btdSpawnX");
        m.btdSpawnY = nbt.getDouble("btdSpawnY");
        m.btdSpawnZ = nbt.getDouble("btdSpawnZ");
        return m;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putBoolean("defaultKeepInv",    defaultKeepInv);
        nbt.putBoolean("clearDreamItems",   clearDreamItems);
        nbt.putDouble("btdSpawnX",         btdSpawnX);
        nbt.putDouble("btdSpawnY",         btdSpawnY);
        nbt.putDouble("btdSpawnZ",         btdSpawnZ);
        return nbt;
    }

    public static MapVariables get(LevelAccessor world) {
        if (!(world instanceof ServerLevel lvl))
            return new MapVariables();
        return lvl.getDataStorage().computeIfAbsent(
            MapVariables::load,
            MapVariables::new,
            DATA_NAME
        );
    }

    public void syncAll(LevelAccessor world) {
        if (world instanceof ServerLevel lvl) {
            TrueEndMod.PACKET_HANDLER.send(
                PacketDistributor.DIMENSION.with(() -> lvl.dimension()),
                new TrueEndVariables.MapVariablesSyncMessage(this)
            );
        }
    }
}
