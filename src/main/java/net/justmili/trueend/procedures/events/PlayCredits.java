package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.client.CreditsScreen;
import net.justmili.trueend.config.Config;
import net.justmili.trueend.init.Sounds;
import net.justmili.trueend.network.Variables;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayCredits {
    private static boolean tickHandlerEnabled = false;
    private static int ticksUntilShow = -1;
    private static boolean hasShownCreditsThisSession = false;
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (Variables.creditsToggle) hasShownCreditsThisSession = false;
        if (hasShownCreditsThisSession) return;

        if (event.getFrom() == BTD &&
            event.getTo() == Level.OVERWORLD &&
            Variables.creditsToggle) {

            hasShownCreditsThisSession = true;

            ticksUntilShow = 10;
            tickHandlerEnabled = true;
            Config.updateConfig("creditsToggle", false);

            TrueEnd.wait(2, () -> {
                if (mc.player != null) {
                    double x = mc.player.getX();
                    double y = mc.player.getY();
                    double z = mc.player.getZ();
                    playSound(x, y, z);
                }
            });
        }
    }

    public static void playSound(double x, double y, double z) {
        mc.getSoundManager().stop();

        if (mc.level != null && mc.player != null) {
            mc.level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(Sounds.MOD_CREDITS_MUSIC.getId())), SoundSource.MASTER, 1, 1, false);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (!tickHandlerEnabled || event.phase != ClientTickEvent.Phase.END) return;

        if (ticksUntilShow > 0) {
            ticksUntilShow--;
            if (ticksUntilShow == 0) {
                tickHandlerEnabled = false;
                Minecraft mc = Minecraft.getInstance();
                if (mc.screen == null && mc.player != null) {
                    mc.setScreen(new CreditsScreen());
                }
            }
        }
    }
}