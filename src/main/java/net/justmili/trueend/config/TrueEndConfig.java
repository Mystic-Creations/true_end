package net.justmili.trueend.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.justmili.trueend.config.serializer.GsonSerializer;

import java.util.Map;

public class TrueEndConfig {
	public static final GsonSerializer serializer = new GsonSerializer("TrueEnd_COMMON");
	public static Map<String, Object> entries;

	public static ConfigBuilder getConfigBuilder() {
		ConfigBuilder builder = ConfigBuilder.create().setTitle(Component.literal("TrueEnd Config"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		builder.setTitle(Component.translatable("config.true_end.screentitle"));
		builder.setDefaultBackgroundTexture(ResourceLocation.parse("true_end:textures/block/old_planks.png"));
		builder.setShouldTabsSmoothScroll(true);
		builder.setShouldTabsSmoothScroll(true);
		ConfigCategory gameplay = builder.getOrCreateCategory(Component.translatable("config.true_end.category.gameplay"));
		ConfigCategory render = builder.getOrCreateCategory(Component.translatable("config.true_end.category.render"));
		SubCategoryBuilder events = entryBuilder.startSubCategory(Component.translatable("config.true_end.subcategory.events"));
		gameplay.addEntry(events.build());
		builder.setSavingRunnable(() -> serializer.serialize(entries));
		return builder;
	}
}

/**
 * Config options to do:
 * 1. clearDreamItems / GAMEPLAY CATEGORY
 *    - default: true
 *    + Gamerule updating procedure
 * 2. randomEventChance / EVENTS SUBCATEGORY
 *    - default: 0.005
 *    - min:     0.0
 *    - max:     0.1
 * 3. entitySpawnChance / EVENTS SUBCATEGORY
 *    - default: 0.008
 *    - min:     0.0
 *    - max:     0.1
 * 4. popupsEnabled / EVENTS SUBCATEGORY
 *    - default: true
 * 5. fogToggle / RENDER CATEGORY
 *    - default: true
 */