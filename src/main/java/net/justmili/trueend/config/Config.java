package net.justmili.trueend.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.justmili.trueend.network.Variables;
import net.justmili.trueend.config.serializer.GsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Config {
	public static final GsonSerializer serializer = new GsonSerializer("TrueEnd_SERVER");
	public static Map<String, Object> entries;

	public static ConfigBuilder getConfigBuilder() {
		ConfigBuilder builder = ConfigBuilder.create()
				.setTitle(Component.translatable("config.true_end.screentitle"))
				.setDefaultBackgroundTexture(ResourceLocation.parse("true_end:textures/block/old_planks.png"))
				.setSavingRunnable(() -> serializer.serialize(entries));

		ConfigEntryBuilder eb = builder.entryBuilder();
		ConfigCategory gameplay = builder.getOrCreateCategory(Component.translatable("config.true_end.category.gameplay"));
		ConfigCategory events   = builder.getOrCreateCategory(Component.translatable("config.true_end.category.events"));
		ConfigCategory other    = builder.getOrCreateCategory(Component.translatable("config.true_end.category.other"));

		// 1) Conversation delay (no dependency)
		entries.putIfAbsent("btdConversationDelay", 40);
		IntegerListEntry btdDelay = eb
				.startIntField(Component.translatable("config.true_end.entry.btdconvodelay.title"),
						((Number) entries.get("btdConversationDelay")).intValue())
				.setDefaultValue(40)
				.setMin(0).setMax(100)
				.setTooltip(Component.translatable("config.true_end.entry.btdconvodelay.tooltip"))
				.setSaveConsumer(v -> entries.put("btdConversationDelay", v))
				.build();
		Variables.btdConversationDelay = btdDelay;
		gameplay.addEntry(btdDelay);

		// 2) randomEventsToggle (no dependency)
		entries.putIfAbsent("randomEventsToggle", true);
		BooleanListEntry randomEventsToggle = eb
				.startBooleanToggle(Component.translatable("config.true_end.entry.randomevents.title"),
						(Boolean) entries.get("randomEventsToggle"))
				.setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.randomevents.tooltip"))
				.setSaveConsumer(v -> entries.put("randomEventsToggle", v))
				.build();
		Variables.randomEventsToggle = randomEventsToggle;
		gameplay.addEntry(randomEventsToggle);

		// 3) randomEventChance (depends on randomEventsToggle)
		entries.putIfAbsent("randomEventChance", 0.005d);
		DoubleListEntry randomEventChance = eb
				.startDoubleField(Component.translatable("config.true_end.entry.randomeventchance.title"),
						(Double) entries.get("randomEventChance"))
				.setDefaultValue(0.005d)
				.setMin(0.0).setMax(0.1)
				.setTooltip(Component.translatable("config.true_end.entry.randomeventchance.tooltip"))
				.setRequirement(Requirement.isTrue(randomEventsToggle))
				.setSaveConsumer(v -> entries.put("randomEventChance", v))
				.build();
		Variables.randomEventChance = randomEventChance;
		events.addEntry(randomEventChance);

		// 4) fogToggle (no dependency)
		entries.putIfAbsent("fogToggle", true);
		@NotNull BooleanListEntry fogToggle = eb
				.startBooleanToggle(Component.translatable("config.true_end.entry.btdfog.title"),
						(Boolean) entries.get("fogToggle"))
				.setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.btdfog.tooltip"))
				.setSaveConsumer(v -> entries.put("fogToggle", v))
				.build();
		Variables.fogToggle = fogToggle;
		gameplay.addEntry(fogToggle);

		// 5) entitySpawnChance (no dependency)
		entries.putIfAbsent("entitySpawnChance", 0.008d);
		DoubleListEntry entitySpawnChance = eb
				.startDoubleField(Component.translatable("config.true_end.entry.entityspawning.title"),
						(Double) entries.get("entitySpawnChance"))
				.setDefaultValue(0.008d)
				.setMin(0.0).setMax(0.1)
				.setTooltip(Component.translatable("config.true_end.entry.entityspawning.tooltip"))
				.setSaveConsumer(v -> entries.put("entitySpawnChance", v))
				.build();
		Variables.entitySpawnChance = entitySpawnChance;
		events.addEntry(entitySpawnChance);

		// 6) popupsToggle (depends on randomEventsToggle)
		entries.putIfAbsent("popupsToggle", true);
		BooleanListEntry popupsToggle = eb
				.startBooleanToggle(Component.translatable("config.true_end.entry.pop_ups.title"),
						(Boolean) entries.get("popupsToggle"))
				.setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.pop_ups.tooltip"))
				.setRequirement(Requirement.isTrue(randomEventsToggle))
				.setSaveConsumer(v -> entries.put("popupsToggle", v))
				.build();
		Variables.popupsToggle = popupsToggle;
		events.addEntry(popupsToggle);

		// 7) creditsToggle (no dependency)
		entries.putIfAbsent("creditsToggle", true);
		BooleanListEntry creditsToggle = eb
				.startBooleanToggle(Component.translatable("config.true_end.entry.credits.title"),
						(Boolean) entries.get("creditsToggle"))
				.setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.credits.tooltip"))
				.setSaveConsumer(v -> entries.put("creditsToggle", v))
				.build();
		Variables.creditsToggle = creditsToggle;
		other.addEntry(creditsToggle);

		return builder;
	}
	public static void load() {
		entries = serializer.deserialize();
	}
}