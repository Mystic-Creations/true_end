package net.justmili.trueend.procedures.events.worldupdates;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import static net.justmili.trueend.regs.DimKeyRegistry.NWAD;

@Mod.EventBusSubscriber
public class UpdateDefaultKeepInvOnLogin {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

    private static void execute(@Nullable Event event, Entity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
		ServerLevel world = (ServerLevel) player.level();
		if (!((entity.level().dimension()) == NWAD)) {
			boolean getKeepInventory = world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
			TrueEndVariables.MapVariables.get(world).setDefaultKeepInv(getKeepInventory);
		}
	}
}
