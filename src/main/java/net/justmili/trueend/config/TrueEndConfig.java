package net.justmili.trueend.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.justmili.trueend.network.TrueEndVariables;
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
		ConfigCategory events = builder.getOrCreateCategory(Component.translatable("config.true_end.category.events"));
		ConfigCategory render = builder.getOrCreateCategory(Component.translatable("config.true_end.category.render"));
		//entries.putIfAbsent("clearDreamItems", true);
		//TrueEndVariables.clearDreamItems = entryBuilder.startBooleanToggle(Component.translatable("config.true_end.entry.cleardreamitems.title"), (boolean) entries.get("clearDreamItems")).setDefaultValue(true)
		//		.setTooltip(Component.translatable("config.true_end.entry.cleardreamitems.tooltip")).setSaveConsumer(newValue -> entries.put("clearDreamItems", newValue)).build();
		entries.putIfAbsent("btdConversationDelay", /*@int*/40);
		TrueEndVariables.btdConversationDelay = entryBuilder.startIntField(Component.translatable("config.true_end.entry.btdconvodelay.title"), Double.valueOf(String.valueOf(entries.get("btdConversationDelay"))).intValue()).setDefaultValue(/*@int*/40)
				.setTooltip(Component.translatable("config.true_end.entry.btdconvodelay.tooltip")).setMin(/*@int*/0).setMax(/*@int*/100).setSaveConsumer(newValue -> entries.put("btdConversationDelay", newValue)).build();
		entries.putIfAbsent("randomEventChance", 0.005d);
		TrueEndVariables.randomEventChance = entryBuilder.startDoubleField(Component.translatable("config.true_end.entry.randomeventchance.title"), (double) entries.get("randomEventChance")).setDefaultValue(0.005d)
				.setTooltip(Component.translatable("config.true_end.entry.randomeventchance.tooltip")).setMin(/*@int*/0).setMax(0.1).setRequirement(Requirement.isTrue(TrueEndVariables.randomEventsToggle))
				.setSaveConsumer(newValue -> entries.put("randomEventChance", newValue)).build();
		entries.putIfAbsent("entitySpawnChance", 0.008d);
		TrueEndVariables.entitySpawnChance = entryBuilder.startDoubleField(Component.translatable("config.true_end.entry.entityspawning.title"), (double) entries.get("entitySpawnChance")).setDefaultValue(0.008d)
				.setTooltip(Component.translatable("config.true_end.entry.entityspawning.tooltip")).setMin(/*@int*/0).setMax(0.1).setSaveConsumer(newValue -> entries.put("entitySpawnChance", newValue)).build();
		entries.putIfAbsent("randomEventsToggle", true);
		TrueEndVariables.randomEventsToggle = entryBuilder.startBooleanToggle(Component.translatable("config.true_end.entry.randomevents.title"), (boolean) entries.get("randomEventsToggle")).setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.randomevents.tooltip")).setSaveConsumer(newValue -> entries.put("randomEventsToggle", newValue)).build();
		entries.putIfAbsent("popupsToggle", true);
		TrueEndVariables.popupsToggle = entryBuilder.startBooleanToggle(Component.translatable("config.true_end.entry.pop_ups.title"), (boolean) entries.get("popupsToggle")).setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.pop_ups.tooltip")).setRequirement(Requirement.isTrue(TrueEndVariables.randomEventsToggle)).setSaveConsumer(newValue -> entries.put("popupsToggle", newValue)).build();
		entries.putIfAbsent("fogToggle", true);
		TrueEndVariables.fogToggle = entryBuilder.startBooleanToggle(Component.translatable("config.true_end.entry.btdfog.title"), (boolean) entries.get("fogToggle")).setDefaultValue(true)
				.setTooltip(Component.translatable("config.true_end.entry.btdfog.tooltip")).setSaveConsumer(newValue -> entries.put("fogToggle", newValue)).build();
		//gameplay.addEntry(TrueEndVariables.clearDreamItems);
		gameplay.addEntry(TrueEndVariables.randomEventsToggle);
		gameplay.addEntry(TrueEndVariables.btdConversationDelay);
		events.addEntry(TrueEndVariables.randomEventChance);
		events.addEntry(TrueEndVariables.entitySpawnChance);
		events.addEntry(TrueEndVariables.popupsToggle);
		render.addEntry(TrueEndVariables.fogToggle);
		builder.setSavingRunnable(() -> serializer.serialize(entries));
		return builder;
	}
}