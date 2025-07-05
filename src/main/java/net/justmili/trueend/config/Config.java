package net.justmili.trueend.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.justmili.trueend.network.Variables;
import net.justmili.trueend.config.serializer.GsonSerializer;

import java.util.Map;

public class Config {
	public static final GsonSerializer serializer = new GsonSerializer("TrueEnd_COMMON");
	public static Map<String, Object> entries;

	public static void load() {
		entries = serializer.deserialize();
	}
}