package net.justmili.trueend.procedures.events;

import net.justmili.trueend.client.TrueEndCreditsScreen;
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

import static net.justmili.trueend.TrueEnd.MODID;
import static net.justmili.trueend.config.TrueEndConfig.entries;
import static net.justmili.trueend.network.TrueEndVariables.creditsToggle;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreditsDetect {


    private static boolean tickHandlerEnabled = false;
    private static int ticksUntilShow = -1;

    @SubscribeEvent
    public static void onDimensionChange(PlayerChangedDimensionEvent event) {
        if (event.getFrom() == DimKeyRegistry.BTD && event.getTo() == Level.OVERWORLD && TrueEndVariables.creditsToggle.getValue() == true) {
            ticksUntilShow = 10;
            tickHandlerEnabled = true;   

            Minecraft mc = Minecraft.getInstance();

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
                    mc.setScreen(new TrueEndCreditsScreen());
                }
                //Change this so the change will be permanent and not just for the time of the game session
                TrueEndConfig.entries.put("creditsToggle", false);
            }
        }
    }
}
