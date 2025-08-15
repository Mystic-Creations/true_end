package net.justmili.trueend.config;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.config.serializer.GsonSerializer;
import net.justmili.trueend.init.Packets;
import net.justmili.trueend.network.Variables;
import net.justmili.trueend.network.packets.UpdateClientConfigPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber
public class Config {
	public static final GsonSerializer serializer = new GsonSerializer("TrueEnd_COMMON");
	public static Map<String, Object> entries;

	public static void updateConfig(String key, Object value) {
		entries.put(key, value);
		serializer.serialize(entries);

		switch (key) {
			case "randomEventChance" -> Variables.randomEventChance = (double)  value;
			case "entitySpawnChance" -> Variables.entitySpawnChance = (double)  value;
			case "btdConversationDelay" -> Variables.btdConversationDelay = (int) value;
			case "creditsToggle" -> Variables.creditsToggle = (boolean) value;
			case "fogToggle" -> Variables.fogToggle = (boolean) value;
			case "popupsToggle" -> Variables.popupsToggle = (boolean) value;
			case "daytimeChangeToggle" -> Variables.daytimeChangeToggle = (boolean) value;
			case "clearDreamItems" -> Variables.clearDreamItems = (boolean) value;
			case "flashingLights" -> Variables.flashingLights = (boolean) value;
			default -> TrueEnd.LOGGER.warn("updateConfig: unhandled key '{}'", key);
		}
	}

	public static int handleBoolean(CommandSourceStack src, String key, boolean value) {
		try {
			Config.updateConfig(key, value);
		} catch (Exception e) {
			src.sendFailure(Component.literal("Failed to update config '" + key + "': " + e.getMessage()));
			return 0;
		}
		src.sendSuccess(() -> Component.literal("Config '" + key + "' is now set to " + value), false);
		return 1;
	}
	public static int handleDouble(CommandSourceStack src, String key, double value) {
		try {
			Config.updateConfig(key, value);
		} catch (Exception e) {
			src.sendFailure(Component.literal("Failed to update config '" + key + "': " + e.getMessage()));
			return 0;
		}
		src.sendSuccess(() -> Component.literal("Config '" + key + "' is now set to " + value), false);
		return 1;
	}
	public static int handleInt(CommandSourceStack src, String key, int value) {
		try {
			Config.updateConfig(key, value);
		} catch (Exception e) {
			src.sendFailure(Component.literal("Failed to update config '" + key + "': " + e.getMessage()));
			return 0;
		}
		src.sendSuccess(() -> Component.literal("Config '" + key + "' is now set to " + value), false);
		return 1;
	}

	public static void updateClientConfig(ServerPlayer player, CommandSourceStack src, String key, boolean value) {
		updateConfig(key, value);
		handleBoolean(src, key, value);

		if (player != null && !player.level().isClientSide()) {
			Packets.sendToPlayer(
					new UpdateClientConfigPacket(key, value), player);
		}
	}
}