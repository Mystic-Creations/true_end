package net.justmili.trueend;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import net.justmili.trueend.init.TrueEndModTabs;
import net.justmili.trueend.init.TrueEndModParticleTypes;
import net.justmili.trueend.init.TrueEndModItems;
import net.justmili.trueend.init.TrueEndModBlocks;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

@Mod("true_end")
public class TrueEndMod {
	public static final Logger LOGGER = LogManager.getLogger(TrueEndMod.class);
	public static final String MODID = "true_end";

	public TrueEndMod() {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		TrueEndModBlocks.REGISTRY.register(bus);

		TrueEndModItems.REGISTRY.register(bus);

		TrueEndModBlocks.REGISTRY.register(bus);

		TrueEndModTabs.REGISTRY.register(bus);

		TrueEndModParticleTypes.REGISTRY.register(bus);

		// Start of user code block mod init
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}

	public static void sendTellrawFormatMessage(ServerPlayer player, String message) {
		JsonElement jsonElement;
		try {
			jsonElement = JsonParser.parseString(message);
		} catch (JsonSyntaxException e) {
			LOGGER.error("MESSAGE FAILED TO SEND BECAUSE YOU FORMATTED TELLRAW STYLE TEXT WRONG");
			String json =  "JSON in question: %s".formatted(message);
			LOGGER.error(json);
			LOGGER.error(e.getMessage());
			return;
		}
		// convert message to components
		Component component = Component.Serializer.fromJson(jsonElement);
		if (component != null) {
			player.sendSystemMessage(component);
		}
	}

	public static void sendTellrawMessagesWithCooldown(ServerPlayer player, String[] messages, int cooldown) {
		for (int i = 0; i < messages.length;i++) {
			String message = messages[i];
			queueServerWork(	1+ cooldown * i, () -> {
				sendTellrawFormatMessage(player, message);
			});
		}
	}

	private static Predicate<Holder<Biome>> isBiome(String biomeNamespaced) {
		return biomeHolder -> biomeHolder.unwrapKey().map(biomeKey ->
				biomeKey.location().toString().equals(biomeNamespaced)
		).orElse(false);
	};

	public static BlockPos locateBiome(ServerLevel level,BlockPos startPosition, String biomeNamespaced) {

		Pair<BlockPos, Holder<Biome>> blockPosHolderPair = level.getLevel().findClosestBiome3d(isBiome(biomeNamespaced), startPosition, 6400, 32, 64);
		if (blockPosHolderPair == null) {
			return null;
		}
		return blockPosHolderPair.getFirst();
	}
}
