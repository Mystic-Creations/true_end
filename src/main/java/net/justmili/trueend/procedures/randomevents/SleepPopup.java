package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.interfaces.User32;
import net.justmili.trueend.network.Variables;
import net.justmili.trueend.TrueEnd;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.*;

@Mod.EventBusSubscriber
public class SleepPopup {
	@SubscribeEvent
	public static void onPlayerInBed(PlayerSleepInBedEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Player player) {
		execute(null, world, player);
	}

	private static void execute(@Nullable Object event, LevelAccessor world, Player player) {
		if (player.level().dimension() != Level.OVERWORLD)
			return;

		if (player.isSleeping()) {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				if (Variables.randomEventsToggle
						&& Variables.popupsToggle
						&& Math.random() < Variables.randomEventChance) {
					TrueEnd.wait(20, () -> {
						User32.INSTANCE.MessageBoxA(0L, "wake up.", "", 0);
					});
				}
			} else {
				if (Variables.randomEventsToggle
						&& Variables.popupsToggle
						&& Math.random() < Variables.randomEventChance) {
					TrueEnd.wait(20, () -> {
						SwingUtilities.invokeLater(() -> {
							String message = "wake up.";
							String title = "";

							JOptionPane pane = new JOptionPane(
									message,
									JOptionPane.INFORMATION_MESSAGE
							);

							JDialog dialog = pane.createDialog(null, title);
							dialog.setAlwaysOnTop(true);
							dialog.setModal(true);
							dialog.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
							dialog.setVisible(true);
						});
					});
				}
			}
		}
	}
}