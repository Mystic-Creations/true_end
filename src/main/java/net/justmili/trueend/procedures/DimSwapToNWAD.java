package net.justmili.trueend.procedures;

import net.justmili.trueend.network.Variables;
import net.justmili.trueend.sources.invmgr.PlayerInvManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static net.justmili.trueend.init.Dimensions.NWAD;

@Mod.EventBusSubscriber
public class DimSwapToNWAD {
	private static final Map<UUID, ResourceKey<Level>> diedIn = new HashMap<>();
	private static final Random RAND = new Random();

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event == null || event.getEntity() == null) return;
		executeHurt(event.getEntity().level(), event.getSource(), event.getEntity());
	}

	private static void executeHurt(Level world, DamageSource source, Entity entity) {
		if (world == null || source == null || entity == null) return;
		if (!(entity instanceof ServerPlayer player)) return;
		if (!source.is(DamageTypes.IN_WALL)) return;
		if (RAND.nextDouble() >= Variables.randomEventChance * 2) return;

		Advancement adv = player.server.getAdvancements()
				.getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
		boolean hasAdvancement = adv != null && player.getAdvancements().getOrStartProgress(adv).isDone();
		if (hasAdvancement) return;

		PlayerInvManager.saveInvNWAD(player);
		if (!world.isClientSide()) {
			//player.getInventory().clearContent();
			//I turned off item clearing so player will have a bit
			// more of a scare about losing their items even tho they're gonna get them back
			player.setGameMode(GameType.ADVENTURE);
			teleportToNWAD(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof ServerPlayer player)) return;
		ResourceKey<Level> dim = player.level().dimension();
		if (dim == NWAD) {
			diedIn.put(player.getUUID(), dim);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof ServerPlayer player)) return;

		UUID uuid = player.getUUID();
		ResourceKey<Level> dim = diedIn.remove(uuid);
		if (dim == null || dim != NWAD) return;

		PlayerInvManager.restoreInv(player);
		player.setGameMode(GameType.SURVIVAL);

		Advancement advancement = player.server.getAdvancements()
				.getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
        assert advancement != null;
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		if (!progress.isDone()) {
			for (String criteria : progress.getRemainingCriteria()) {
				player.getAdvancements().award(advancement, criteria);
			}
		}
	}

	private static void teleportToNWAD(ServerPlayer player) {
		if (player.level().dimension() == NWAD) return;
		ServerLevel next = player.server.getLevel(NWAD);
		if (next == null) return;

		player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

		int x = player.getBlockX(), z = player.getBlockZ(), y = 120;
		boolean found = false;
		while (y > 0) {
			BlockPos pos = new BlockPos(x, y, z);
			if (next.isEmptyBlock(pos)
					&& next.isEmptyBlock(pos.above())
					&& !next.isEmptyBlock(pos.below())) {
				found = true;
				break;
			}
			y--;
		}
		if (!found) y = 129;

		player.teleportTo(next, x, y + 1, z, player.getYRot(), player.getXRot());
		player.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
		player.getActiveEffects().forEach(e ->
				player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), e))
		);
		player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
	}
}
