package net.justmili.trueend.procedures.randomevents;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.StdCallLibrary;
import net.justmili.trueend.network.TrueEndVariables;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import net.minecraft.world.level.LevelAccessor;

import net.justmili.trueend.TrueEnd;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerLead {
	@SubscribeEvent
	public static void onPlayerInBed(PlayerSleepInBedEvent event) {
		execute(event, event.getEntity().level());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (TrueEndVariables.randomEventsToggle.getValue() == true) {
			if (TrueEndVariables.popupsToggle.getValue() == true) {
				if (Math.random() < 0.2) {
					TrueEnd.queueServerWork(20, () -> {
						User32.INSTANCE.MessageBoxA(0L, "wake up.", " ", 0);
					});
				}
			}
		}
	}
	public static interface User32
			extends StdCallLibrary {
		public static final User32 INSTANCE = (User32) Native.load((String)"user32", User32.class);

		public int MessageBoxA(long var1, String var3, String var4, int var5);
	}
}
