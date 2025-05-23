package net.justmili.trueend.config;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;

import net.justmili.trueend.TrueEnd;

import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "true_end", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrueEndConfigInit {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((client, screen) -> TrueEndConfig.getConfigBuilder().setParentScreen(screen).build())));
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		Map<String, Object> entries = TrueEndConfig.serializer.deserialize();
		TrueEndConfig.entries = entries == null ? new HashMap<>() : entries;
		TrueEndConfig.getConfigBuilder();
		TrueEndConfig.serializer.serialize(TrueEndConfig.entries);
		TrueEnd.LOGGER.atLevel(Level.DEBUG).withMarker(MarkerManager.getMarker("CONFIG")).log(TrueEndConfig.serializer.getMessage());
	}
}
