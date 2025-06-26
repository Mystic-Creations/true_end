package net.justmili.trueend.procedures.events.worldupdates;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraft.world.level.GameRules;

import static net.justmili.trueend.init.Dimensions.NWAD;

@Mod.EventBusSubscriber
public class UpdateDefaultKeepInv {
	@SubscribeEvent
	public static void onCommand(CommandEvent event) {
		// Skip updates if in Nightmare dimension
		try {
			ServerLevel lvl = event.getParseResults()
					.getContext()
					.getSource()
					.getLevel();
			if (lvl.dimension() == NWAD) {
				TrueEnd.LOGGER.info("[DEBUG] defaultKeepInv: Skipped update in NWAD");
				return;
			}
		} catch (Exception ignored) {}

		TrueEnd.queueServerWork(30, () -> {
			String fullCommand = event.getParseResults().getReader().getString().trim();
			if (fullCommand.equalsIgnoreCase("gamerule keepInventory")) {
				TrueEnd.LOGGER.info("[DEBUG] defaultKeepInv: Ignored gamerule query without value");
				return;
			}
			if (fullCommand.toLowerCase().startsWith("trueend")) {
				TrueEnd.LOGGER.info("[DEBUG] defaultKeepInv: Ignored update, True End command was used");
				return;
			}
			if (!fullCommand.toLowerCase().startsWith("gamerule keepinventory ")) {
				TrueEnd.LOGGER.error("defaultKeepInv: Ignored update, non-keepInventory command was used");
				return;
			}

			try {
				var source = event.getParseResults().getContext().getSource();
				ServerLevel level = source.getLevel();
				Entity entity = source.getEntity();
				if (entity instanceof Player) {
					boolean keepInv = level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
					TrueEnd.LOGGER.info("[DEBUG] boolean keepInv : got keepInventory as " + keepInv);
					Variables.MapVariables.get(level).setDefaultKeepInv(keepInv);
					TrueEnd.LOGGER.info("[DEBUG] variable defaultKeepInv : set to " + Variables.MapVariables.get(level).isDefaultKeepInv());
				} else {
					TrueEnd.LOGGER.error("defaultKeepInv: Ignored update, source was not a player");
				}
			} catch (Exception e) {
				TrueEnd.LOGGER.error("defaultKeepInv: Source did not provide level or server context", e);
			}
		});
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) return;
		// Skip if login into Nightmare
		ServerLevel level = (ServerLevel) player.level();
		if (level.dimension() == NWAD) {
			TrueEnd.LOGGER.info("[DEBUG] defaultKeepInv: Skipped on login in NWAD");
			return;
		}

		boolean keepInv = level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
		TrueEnd.LOGGER.info("[DEBUG] boolean keepInv : got keepInventory as " + keepInv);
		Variables.MapVariables.get(level).setDefaultKeepInv(keepInv);
		TrueEnd.LOGGER.info("[DEBUG] variable defaultKeepInv : set to " + Variables.MapVariables.get(level).isDefaultKeepInv());
	}
}
