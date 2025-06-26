package net.justmili.trueend.procedures;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.Variables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import javax.annotation.Nullable;
import java.util.Objects;
import static net.justmili.trueend.init.Dimensions.NWAD;


@Mod.EventBusSubscriber
public class DimSwapToNWAD {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event == null || event.getEntity() == null) return;
		executeHurt(event.getEntity().level(), event.getSource(), event.getEntity());
	}

	private static void executeHurt(LevelAccessor world, DamageSource source, Entity entity) {
		if (source == null || entity == null) return;

		boolean preferred = Variables.MapVariables.get(world).isDefaultKeepInv();
		double chance = Variables.randomEventChance.getValue() * 2;
		boolean shouldTrigger = Math.random() < chance;

		boolean isPlayer = entity instanceof Player;
		boolean suffocated = source.is(DamageTypes.IN_WALL);
		boolean hasAdvancement = false;
		if (entity instanceof ServerPlayer sp) {
			Advancement adv = sp.server.getAdvancements()
					.getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
			hasAdvancement = adv != null && sp.getAdvancements().getOrStartProgress(adv).isDone();
		}

		if (isPlayer && suffocated && !hasAdvancement && shouldTrigger) {
			// Toggle keepInventory in this world
			world.getLevelData().getGameRules()
					.getRule(GameRules.RULE_KEEPINVENTORY)
					.set(true, world.getServer());
			TrueEnd.LOGGER.info("[DEBUG] DimSwap / NWAD : set keepInventory as " + world.getLevelData().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY));

			// Perform teleport spawn logic unchanged
			if (entity instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {
				teleportToNWAD(serverPlayer);
			}
		}
	}

	// Death handler in NWAD
	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof ServerPlayer)) return;
		if (!entity.level().dimension().location().toString().equals("true_end:nightmare_within_a_dream")) return;

		TrueEnd.queueServerWork(30, () -> {
			LevelAccessor world = entity.level();
			boolean preferred = Variables.MapVariables.get(world).isDefaultKeepInv();
			TrueEnd.LOGGER.info("[DEBUG] DimSwap / NWAD : setting keepInventory back to preferred " + preferred);
			world.getLevelData().getGameRules()
					.getRule(GameRules.RULE_KEEPINVENTORY)
					.set(preferred, world.getServer());
			TrueEnd.LOGGER.info("[DEBUG] DimSwap / NWAD : Current keepInventory is now " + world.getLevelData().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY) + "preferred being " + preferred);

			// Grant advancement
			ServerPlayer sp = (ServerPlayer) entity;
			Advancement advancement = sp.server.getAdvancements()
					.getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"));
			if (advancement != null) {
				AdvancementProgress advancementProgress = sp.getAdvancements().getOrStartProgress(advancement);
				if (!advancementProgress.isDone()) {
					for (String criteria : advancementProgress.getRemainingCriteria()) {
						sp.getAdvancements().award(advancement, criteria);
					}
				}
			}
		});
	}

	// ==== Helper: teleport logic (unchanged) ====: teleport logic (unchanged) ====
	private static void teleportToNWAD(ServerPlayer player) {
		if (player.level().dimension() == NWAD) return;
		ServerLevel next = player.server.getLevel(NWAD);
		if (next == null) return;

		// Tell the client to trigger the "game event" WIN_GAME (clears screen/fade effect) so the teleport transition looks seamless
		player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));

		// Find safe Y
		int y = 120;
		boolean found = false;
		while (y > 0) {
			BlockPos pos = new BlockPos(player.getBlockX(), y, player.getBlockZ());
			if (next.isEmptyBlock(pos) && next.isEmptyBlock(pos.above()) && !next.isEmptyBlock(pos.below())) {
				found = true;
				break;
			}
			y--;
		}
		if (!found) y = 129;

		player.teleportTo(next, player.getBlockX(), y + 1, player.getBlockZ(), player.getYRot(), player.getXRot());

		player.connection.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
		player.getActiveEffects().forEach(e ->
				player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), e))
		);
		player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
	}
}
