package net.justmili.trueend.config;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "true_end", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigInit {
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		Map<String, Object> entries = Config.serializer.deserialize();
		Config.entries = entries == null ? new HashMap<>() : entries;
		Config.serializer.serialize(Config.entries);
	}
}