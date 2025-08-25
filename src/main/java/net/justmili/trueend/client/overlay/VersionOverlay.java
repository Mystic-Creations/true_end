package net.justmili.trueend.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static net.justmili.trueend.init.Dimensions.BTD;
import static net.justmili.trueend.TrueEnd.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class VersionOverlay {

    private static final String BASE_TEXT = "Minecraft Alpha v1.1.2_10";
    private static final String[] VERSIONS = {
        "Minecraft Alpha v1.1.2",
        "Minecraft Alpha v1.1.2_01",
        "Minecraft v1.20.1",
        "Minecraft Snapshot -3w21a", //Reference to the "Snapshot Null" series on YT
        "Minecraft Beta v1.0.46", //Reference to andrewgames722 channel on YT
        "Minecraft Alpha v1.0.7", //Reference to the VoidExp series by mark101 on YT, specifically moonglitch.avi video
        "Minecraft Alpha v1.0.16_02" //Reference to the infamous herobrine screenshot
    };

    private static String currentText = BASE_TEXT;
    private static int flashTicks = 4;
    private static final Random random = new Random();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != BTD) {
            currentText = BASE_TEXT;
            flashTicks = 0;
            return;
        }

        if (flashTicks > 0) {
            flashTicks--;
            if (flashTicks == 0) {
                currentText = BASE_TEXT;
            }
            // Dynamic String Change
            // 6000 - ticks between each random "glitch"
            // "//2-6 ticks" - "glitch" string show time
        } else if (random.nextInt(6000) == 0) {
            currentText = VERSIONS[random.nextInt(VERSIONS.length)];
            flashTicks = 2 + random.nextInt(5); // 2–4 ticks
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != BTD) return;
        if (mc.options.renderDebug) return;

        GuiGraphics gui = event.getGuiGraphics();
        PoseStack pose = gui.pose();

        final int fontSize = 32;
        float guiScaleFactor = (float) mc.getWindow().getScreenWidth() / (float) mc.getWindow().getGuiScaledWidth();
        float baseFontHeight = (float) mc.font.lineHeight;
        float userScale = fontSize / baseFontHeight;

        pose.pushPose();
        pose.scale(1f / guiScaleFactor, 1f / guiScaleFactor, 1f);
        pose.scale(userScale, userScale, 1f);

        int x = 6;
        int y = 6;
        int textColor = 0xFFFFFF;
        int textShadowColor = 0xFF3F3F3F;
        int drawX = Math.round(x / userScale);
        int drawY = Math.round(y / userScale);

        gui.drawString(mc.font, Component.literal(currentText), drawX + 1, drawY + 1, textShadowColor, false);
        gui.drawString(mc.font, Component.literal(currentText), drawX, drawY, textColor, false);

        pose.popPose();
    }
}
