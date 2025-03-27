package net.justmili.trueend.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.AdvancementEvent;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.justmili.trueend.init.TrueEndModGameRules;
import net.justmili.trueend.TrueEndMod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CheckIfExitingEndProcedure {
	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME) == false) {
			if ((world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel worldGenLevel ? worldGenLevel.getLevel().dimension() : Level.OVERWORLD)) == Level.OVERWORLD && entity instanceof ServerPlayer player
					&& player.level() instanceof ServerLevel && player.getAdvancements().getOrStartProgress(player.server.getAdvancements().getAdvancement(new ResourceLocation("true_end:stop_dreaming"))).isDone()) {
				if (entity instanceof ServerPlayer _player && !_player.level().isClientSide()) {
					ResourceKey<Level> destinationType = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("true_end:beyond_the_dream"));
					if (_player.level().dimension() == destinationType)
						return;
					ServerLevel nextLevel = _player.server.getLevel(destinationType);
					if (nextLevel != null) {
						_player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
						_player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
						_player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));
						for (MobEffectInstance _effectinstance : _player.getActiveEffects())
							_player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
						_player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
					}
				}
				if (world.getLevelData().getGameRules().getBoolean(TrueEndModGameRules.CLEAR_DREAM_ITEMS) == true) {
					if (entity instanceof Player _player)
						_player.getInventory().clearContent();
					world.getLevelData().getGameRules().getRule(TrueEndModGameRules.CLEAR_DREAM_ITEMS).set(false, world.getServer());

					String[] convesation = {
							"[\"\",{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\"? You've awakened.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
							"{\"text\":\"So soon, thought it'd dream longer...\",\"color\":\"dark_aqua\"}".formatted(player.getName().getString()),
							"[\"\",{\"text\":\"Well, it's beyond the dream now. The player, \",\"color\":\"dark_green\"},{\"selector\":\"%s\",\"color\":\"dark_green\"},{\"text\":\", woke up.\",\"color\":\"dark_green\"}]".formatted(player.getName().getString()),
							"{\"text\":\"We left something for you in your home.\",\"color\":\"dark_aqua\"}".formatted(player.getName().getString()),
							"{\"text\":\"Use it well.\",\"color\":\"dark_aqua\"}".formatted(player.getName().getString()),
							"{\"text\":\"You may go back to the dream, a dream of a better world if you wish.\",\"color\":\"dark_green\"}".formatted(player.getName().getString()),
							"[\"\",{\"text\":\"We'll see you again soon, \",\"color\":\"dark_aqua\"},{\"selector\":\"%s\",\"color\":\"dark_aqua\"},{\"text\":\".\",\"color\":\"dark_aqua\"}]".formatted(player.getName().getString())
					};

					TrueEndMod.queueServerWork(44, () -> {
						TrueEndMod.sendTellrawMessagesWithCooldown(player, convesation, world.getLevelData().getGameRules().getRule(TrueEndModGameRules.BTD_CONVERSATION_MESSEGE_DELAY).get());
					});

					world.getLevelData().getGameRules().getRule(TrueEndModGameRules.LOGIC_HAS_VISITED_BTD_FOR_THE_FIRST_TIME).set(true, world.getServer());
				}
			}
		}
	}
}
