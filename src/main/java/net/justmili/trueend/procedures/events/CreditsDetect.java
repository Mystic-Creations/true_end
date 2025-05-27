package net.justmili.trueend.procedures.events;

import net.justmili.trueend.client.TrueEndCreditsScreen;
import net.justmili.trueend.config.TrueEndConfig;
import net.justmili.trueend.regs.DimKeyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.network.TrueEndVariables.creditsToggle;
import static net.justmili.trueend.TrueEnd.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreditsDetect {
    private static ResourceKey<Level> lastDimension = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ResourceKey<Level> current = mc.player.level().dimension();
        if (lastDimension == DimKeyRegistry.BTD
                && current == Level.OVERWORLD
                && creditsToggle.getValue()) {

            System.out.println("[DEBUG] " + MODID + ": Leaving " + lastDimension);
            System.out.println("[DEBUG] " + MODID + ": Entering " + current);
            System.out.println("[DEBUG] " + MODID + ": Current creditsToggle value: " + creditsToggle.getValue());

            // 1) Play Pigstep immediately:
            mc.getSoundManager()
                    .play(SimpleSoundInstance.forMusic(SoundEvents.MUSIC_DISC_PIGSTEP));

            // 2) Open the screen *directly*:
            mc.setScreen(new TrueEndCreditsScreen());

            // 3) Flip your toggle off:
            TrueEndConfig.entries.put("creditsToggle", false);
            System.out.println("[DEBUG] " + MODID + ": New creditsToggle value: " + creditsToggle.getValue());
        }

        lastDimension = current;
    }
}

