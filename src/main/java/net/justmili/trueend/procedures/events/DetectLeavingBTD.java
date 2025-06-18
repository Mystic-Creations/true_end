package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.client.CreditsScreen;
import net.justmili.trueend.config.TrueEndConfig;
import net.justmili.trueend.network.TrueEndVariables;
import net.justmili.trueend.regs.DimKeyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DetectLeavingBTD {
    private static boolean tickHandlerEnabled = false;
    private static int ticksUntilShow = -1;
    private static boolean hasShownCreditsThisSession = false;

    @SubscribeEvent
    public static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (hasShownCreditsThisSession) return;

        if (event.getFrom() == DimKeyRegistry.BTD &&
            event.getTo() == Level.OVERWORLD &&
            TrueEndVariables.creditsToggle.getValue()) {

            hasShownCreditsThisSession = true;

            ticksUntilShow = 10;
            tickHandlerEnabled = true;
            TrueEndConfig.entries.put("creditsToggle", false);
            TrueEndConfig.serializer.serialize(TrueEndConfig.entries);

            Minecraft mc = Minecraft.getInstance();
            TrueEnd.queueServerWork(2, () -> {
                if (mc != null) {
                    mc.getSoundManager().stop();

                    if (mc.level != null && mc.player != null) {
                        mc.level.playLocalSound(
                            mc.player.getX(),
                            mc.player.getY(),
                            mc.player.getZ(),
                            SoundEvents.MUSIC_DISC_PIGSTEP,
                            SoundSource.MUSIC,
                            1.0f,
                            1.0f,
                            false
                        );
                    }
                }
            });
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