package net.mysticcreations.true_end.procedures.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import static net.mysticcreations.true_end.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class FeelingWatched {
	@SubscribeEvent
	public static void onEntityEndSleep(PlayerWakeUpEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player player && !player.level().isClientSide() && (entity.level().dimension()) == BTD)
			player.displayClientMessage(Component.translatable("events.true_end.feelingwatched"), true);
	}
}
