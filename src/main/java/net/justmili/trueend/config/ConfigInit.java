package net.justmili.trueend.config;

import net.justmili.trueend.network.Variables;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.Level;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.justmili.trueend.TrueEnd;

import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "true_end", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigInit {
	@SubscribeEvent
	public static void clientSetup(FMLCommonSetupEvent event) {
		Map<String, Object> entries = Config.serializer.deserialize();
		entries = entries == null ? new HashMap<>() : entries;

		entries.putIfAbsent("randomEventChance", 0.005d);
		entries.putIfAbsent("entitySpawnChance", 0.008d);
		entries.putIfAbsent("popupsToggle", true);
		entries.putIfAbsent("fogToggle", true);
		entries.putIfAbsent("creditsToggle", true);
		entries.putIfAbsent("btdConversationDelay", 40d);
		entries.putIfAbsent("randomEventsToggle", true);

		//Variables.clearDreamItems = (boolean) entries.get("clearDreamItems");
		Variables.randomEventChance = (double) entries.get("randomEventChance");
		Variables.entitySpawnChance = (double) entries.get("entitySpawnChance");
		Variables.popupsToggle = (boolean) entries.get("popupsToggle");
		Variables.fogToggle = (boolean) entries.get("fogToggle");
		Variables.creditsToggle = (boolean) entries.get("creditsToggle");
		Variables.btdConversationDelay = Math.toIntExact(Math.round((double) entries.get("btdConversationDelay")));
		Variables.randomEventsToggle = (boolean) entries.get("randomEventsToggle");

		Config.entries = entries;
		Config.serializer.serialize(Config.entries);
		TrueEnd.LOGGER.atLevel(Level.DEBUG).withMarker(MarkerManager.getMarker("CONFIG")).log(Config.serializer.getMessage());

	}
}