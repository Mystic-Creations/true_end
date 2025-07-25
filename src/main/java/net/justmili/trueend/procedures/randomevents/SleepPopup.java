package net.justmili.trueend.procedures.randomevents;

import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.justmili.trueend.interfaces.User32;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SleepPopup {

@SubscribeEvent
public static void onPlayerInBed(PlayerSleepInBedEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;

    System.out.println("Sleep event fired!");

    new Thread(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            User32.INSTANCE.MessageBoxA(0L, "wake up.", "", 0);
        } else {
            SwingUtilities.invokeLater(() -> {
                JOptionPane pane = new JOptionPane("wake up.", JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = pane.createDialog(null, "");
                dialog.setAlwaysOnTop(true);
                dialog.setModal(true);
                dialog.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
                dialog.setVisible(true);
            });
        }
    }).start();
  }
}