package net.justmili.trueend.procedures.events;

import net.justmili.trueend.network.Variables;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class NoBtdEscape {
    private static final Map<UUID, ResourceKey<Level>> diedIn = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;
        diedIn.put(player.getUUID(), player.level().dimension());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Entity entity = event.getEntity();
        LevelAccessor world = entity.level();
        if (!(entity instanceof ServerPlayer player)) return;

        UUID uuid = player.getUUID();
        ResourceKey<Level> dim = diedIn.remove(uuid);
        if (dim == null || dim != BTD) return;

        double bx = Variables.MapVariables.get(world).getBtdSpawnX();
        double by = Variables.MapVariables.get(world).getBtdSpawnY();
        double bz = Variables.MapVariables.get(world).getBtdSpawnZ();
        ServerLevel btd = player.server.getLevel(BTD);
        if (btd == null) return;

        player.teleportTo(btd, bx, by+0.2, bz, 0, 0);
    }
}
