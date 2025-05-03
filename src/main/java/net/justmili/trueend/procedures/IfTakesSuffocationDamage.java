package net.justmili.trueend.procedures;

import net.justmili.trueend.network.TrueEndVariables;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import static net.justmili.trueend.regs.DimKeyRegistry.NWAD;

@Mod.EventBusSubscriber
public class IfTakesSuffocationDamage {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getSource(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, DamageSource damagesource, Entity entity) {
		execute(null, world, damagesource, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, DamageSource damagesource, Entity entity) {
		if (damagesource == null || entity == null)
			return;
		boolean currentKeepInv = TrueEndVariables.MapVariables.get(world).isDefaultKeepInv();
		if (entity instanceof Player && damagesource.is(DamageTypes.IN_WALL) && !(entity instanceof ServerPlayer _plr2 && _plr2.level() instanceof ServerLevel
				&& _plr2.getAdvancements().getOrStartProgress(_plr2.server.getAdvancements().getAdvancement(ResourceLocation.parse("true_end:leave_the_nightmare_within_a_dream"))).isDone())) {
			if (currentKeepInv == true) {
				world.getLevelData().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, world.getServer());
			} else if (currentKeepInv == false) {
				world.getLevelData().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(false, world.getServer());
			}
			if (entity instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {
				ResourceKey<Level> destinationType = NWAD;
				if (serverPlayer.level().dimension() == destinationType)
					return;
				ServerLevel nextLevel = serverPlayer.server.getLevel(destinationType);
				if (nextLevel != null) {
					serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
					// Find a location for the player to stand on
					// start at 120 to avoid spawning at bedrock roof
					int y = 120;
					boolean foundPlace = false;

					while (y > 0) {
						BlockPos pos = new BlockPos(serverPlayer.getBlockX(), y, serverPlayer.getBlockZ());
						BlockPos posAbove = pos.above();
						BlockPos posBelow = pos.below();

						boolean isEmpty = nextLevel.isEmptyBlock(pos);
						boolean isAboveEmpty = nextLevel.isEmptyBlock(posAbove);
						boolean isBelowSolid = !nextLevel.isEmptyBlock(posBelow);

						if (isEmpty && isAboveEmpty && isBelowSolid) {
							foundPlace = true;
							break;
						}
						y--;
					}

					// If no valid spot is found, set a fallback
					if (!foundPlace) {
						y = 129;
					}

					serverPlayer.teleportTo(nextLevel, serverPlayer.getBlockX(), y + 1, serverPlayer.getBlockZ(), serverPlayer.getYRot(), serverPlayer.getXRot());

					serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
					for (MobEffectInstance mobEffectInstance : serverPlayer.getActiveEffects())
						serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), mobEffectInstance));
					serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
				}
			}
		}
	}
}