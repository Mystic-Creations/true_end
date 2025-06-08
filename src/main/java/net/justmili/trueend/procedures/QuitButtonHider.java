package net.justmili.trueend.procedures.devcmd;

import java.util.Locale;

import net.justmili.trueend.TrueEnd;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
<<<<<<< HEAD
=======
import net.minecraft.network.chat.Component;
>>>>>>> e9834d8e4e7a33b7a618d3acdf446de8b0b54ca8
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "true_end", value = Dist.CLIENT)
public class QuitButtonHider {

    @SubscribeEvent
    public static void onInitScreen(ScreenEvent.Init event) {
        if (!TrueEnd.quitDisabled) return;

        if (event.getScreen() instanceof TitleScreen || event.getScreen() instanceof PauseScreen) {
            for (GuiEventListener listener : event.getScreen().children()) {
                if (listener instanceof Button button) {
                    String msg = button.getMessage().getString().toLowerCase(Locale.ROOT);
                    if (msg.contains("quit") || msg.contains("save and quit")) {
                        button.setMessage(Component.literal("YOU SHALL NOT PASS"));
                        button.active = false;
                    }
                }
            }
        }
    }
}