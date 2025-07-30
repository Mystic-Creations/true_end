package net.justmili.trueend.procedures.events;

import net.justmili.trueend.command.calls.screentests.TestCredits;
import net.justmili.trueend.config.Config;
import net.justmili.trueend.network.Variables;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayCredits {
    private static boolean hasShownCreditsThisSession = false;

    @SubscribeEvent
    public static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (Variables.creditsToggle) hasShownCreditsThisSession = false;
        if (hasShownCreditsThisSession) return;

        if (event.getFrom() == BTD && event.getTo() == Level.OVERWORLD) {
            hasShownCreditsThisSession = true;
            Config.updateConfig("creditsToggle", false);

            TestCredits.execute(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
        }
    }
}