package net.justmili.trueend.procedures.advancements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

@Mod.EventBusSubscriber
public class OpenInventoryAdvancement {
	@SubscribeEvent
	public static void onGuiOpen(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		Minecraft mc = Minecraft.getInstance();
		Player player = event.player;
		boolean isInventory = mc.screen instanceof InventoryScreen;

		if (isInventory) {
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
	}
}
