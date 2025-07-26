package net.justmili.trueend.procedures.randomevents;

import java.awt.Dialog;
import java.awt.GraphicsEnvironment;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.justmili.trueend.interfaces.User32;
import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SleepPopup {
    @SubscribeEvent
    public static void onPlayerInBed(PlayerSleepInBedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!Variables.randomEventsToggle) return;
        if (!Variables.popupsToggle) return;
        if (!(Math.random() < Variables.randomEventChance * 1.5)) return;
        if (player.level().dimension() != Level.OVERWORLD) return;

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String osName = System.getProperty("os.name").toLowerCase();
            boolean isHeadless = GraphicsEnvironment.isHeadless();

            if (osName.contains("win") && !isHeadless) {
                User32.INSTANCE.MessageBoxA(0L, "wake up.", "", 0);
            } else if (!isHeadless) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane pane = new JOptionPane("wake up.", JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = pane.createDialog(null, "");
                    dialog.setAlwaysOnTop(true);
                    dialog.setModal(true);
                    dialog.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
                    dialog.setVisible(true);
                });
            } else {
                System.out.println("[True End] Wake up event triggered (GUI skipped due to headless environment).");
            }
        }).start();
    }
}