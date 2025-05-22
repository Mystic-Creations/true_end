package net.justmili.trueend.procedures.advancements;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.WeakHashMap;

@Mod.EventBusSubscriber
public class OpenInventoryAdvancement {
	// Track the last open container for each player
	private static final WeakHashMap<Player, Boolean> wasInventoryOpen = new WeakHashMap<>();

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		Player player = event.player;
		boolean isInventoryOpen = player.containerMenu instanceof InventoryMenu;
		boolean wasOpen = wasInventoryOpen.getOrDefault(player, false);

		if (!wasOpen && isInventoryOpen) {
			// Player has just opened their inventory
			if (player instanceof ServerPlayer serverPlayer) {
				Advancement advancement = serverPlayer.server.getAdvancements()
						.getAdvancement(ResourceLocation.parse("true_end:story/open_inventory"));
				if (advancement != null) {
					AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
					if (!progress.isDone()) {
						for (String criterion : progress.getRemainingCriteria()) {
							serverPlayer.getAdvancements().award(advancement, criterion);
						}
					}
				}
			}
		}
		wasInventoryOpen.put(player, isInventoryOpen);
	}
}
