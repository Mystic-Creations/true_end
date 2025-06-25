package net.justmili.trueend.procedures.events.worldupdates;

import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.CommandEvent;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import static net.justmili.trueend.init.Dimensions.NWAD;

@Mod.EventBusSubscriber
public class UpdateDefaultKeepInv {
	@SubscribeEvent
	public static void onCommand(CommandEvent event) {
		Entity entity = event.getParseResults().getContext().getSource().getEntity();
		if (entity != null) {
			execute1(event, entity);
		}
	}
	public static void execute1(Entity entity) {
		execute1(null, entity);
	}
	private static void execute1(@Nullable Event event, Entity entity) {
		if (!(entity instanceof ServerPlayer player)) {
			return;
		}
		ServerLevel world = (ServerLevel) player.level();
		if (!((entity.level().dimension()) == NWAD)) {
			boolean getKeepInventory = world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
			Variables.MapVariables.get(world).setDefaultKeepInv(getKeepInventory);
		}
	}
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute2(event, event.getEntity());
	}
	public static void execute2(Entity entity) {
		execute2(null, entity);
	}
	private static void execute2(@Nullable Event event, Entity entity) {
		if (!(entity instanceof ServerPlayer player)) {
			return;
		}
		ServerLevel world = (ServerLevel) player.level();
		if (!((entity.level().dimension()) == NWAD)) {
			boolean getKeepInventory = world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
			Variables.MapVariables.get(world).setDefaultKeepInv(getKeepInventory);
		}
	}
}
