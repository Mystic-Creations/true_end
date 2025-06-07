package net.justmili.trueend.procedures.devcmd;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.justmili.trueend.procedures.QuitButtonControl;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "true_end", value = Dist.CLIENT)
public class QuitButtonHider {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase != ClientTickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof TitleScreen || mc.screen instanceof PauseScreen) {
            for (AbstractWidget widget : mc.screen.children().stream()
                .filter(w -> w instanceof AbstractWidget)
                .map(w -> (AbstractWidget) w).toList()) {
                if (widget instanceof Button button &&
                        (button.getMessage().getString().contains("Quit")
                          || button.getMessage().getString().contains("Save and Quit"))
                        && QuitButtonControl.quitDisabled) {
                    button.active = false;
                    button.visible = false;
                }
            }
        }
    }
}