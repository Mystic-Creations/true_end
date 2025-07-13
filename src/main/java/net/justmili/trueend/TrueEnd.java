package net.justmili.trueend;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;

import net.justmili.trueend.config.Config;
import net.justmili.trueend.entity.Unknown;
import net.justmili.trueend.entity.renderer.UnknownEntityRenderer;
import net.justmili.trueend.init.Blocks;
import net.justmili.trueend.init.Entities;
import net.justmili.trueend.init.Guis;
import net.justmili.trueend.init.Items;
import net.justmili.trueend.init.Particles;
import net.justmili.trueend.init.Sounds;
import net.justmili.trueend.init.Tabs;
import net.justmili.trueend.network.TrueEndModMessages;
import net.justmili.trueend.world.seeping_reality.SeepingForestRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import terrablender.api.Regions;

@Mod("true_end")
public class TrueEnd {
    public static final Logger LOGGER = LogManager.getLogger(TrueEnd.class);
    public static final String MODID = "true_end";

    public TrueEnd(FMLJavaModLoadingContext modContext) {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = modContext.getModEventBus();

        Config.load();

        Items.REGISTRY.register(bus);
        Blocks.REGISTRY.register(bus);
        Tabs.REGISTRY.register(bus);
        Particles.REGISTRY.register(bus);
        Entities.ENTITY_TYPES.register(bus);
        Guis.REGISTRY.register(bus);
        Sounds.REGISTRY.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::onEntityAttributeCreation);
        bus.addListener(UnknownEntityRenderer::registerEntityRenderers);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new SeepingForestRegion(
                    ResourceLocation.parse("true_end:overworld_region"), 1));
            TrueEndModMessages.register();
        });
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

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
            ResourceLocation.parse(MODID),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(Entities.UNKNOWN.get(),
                Unknown.createAttributes().build());
    }

    public static void jsonFormattingCheck(ServerPlayer player, String message) {
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(message);
        } catch (JsonSyntaxException e) {
            LOGGER.error("Something went wrong while reading file: first_entry.txt");
            return;
        }
        Component component = Component.Serializer.fromJson(jsonElement);
        if (component != null) {
            player.sendSystemMessage(component);
        }
    }

    public static void sendMessegeWithCooldown(ServerPlayer player, String[] messages, int cooldown) {
        for (int i = 0; i < messages.length; i++) {
            String msg = messages[i];
            queueServerWork(1 + cooldown * i, () -> jsonFormattingCheck(player, msg));
        }
    }

    private static Predicate<Holder<Biome>> isBiome(String biomeNamespaced) {
        return biomeHolder -> biomeHolder.unwrapKey()
                .map(biomeKey -> biomeKey.location().toString().equals(biomeNamespaced))
                .orElse(false);
    }

    public static BlockPos locateBiome(ServerLevel level, BlockPos startPosition, String biomeNamespaced) {
        Pair<BlockPos, Holder<Biome>> result = level.getLevel()
                .findClosestBiome3d(isBiome(biomeNamespaced), startPosition, 6400, 32, 64);
        if (result == null)
            return null;
        return result.getFirst();
    }
}