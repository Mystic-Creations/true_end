package net.mysticcreations.true_end.procedures.advancement;

import net.mysticcreations.true_end.network.packets.InvOpenAdvPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mysticcreations.true_end.init.Packets;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class OpenInventoryAdvancement {
	private static boolean hasOpenedInventory = false;
	@SubscribeEvent
	public static void onClientTick(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.screen instanceof InventoryScreen) {
			if (!hasOpenedInventory) {
				hasOpenedInventory = true;
				Packets.sendToServer(new InvOpenAdvPacket());
			}
		} else {
			hasOpenedInventory = false;
		}
	}
}