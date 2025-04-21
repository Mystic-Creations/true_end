package net.justmili.trueend.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static net.justmili.trueend.TrueEndMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class VersionOverlay {

    private static final String BASE_TEXT = "Minecraft Alpha v1.1.2_10";
    private static final String[] EGGS = {
        "Minecraft Alpha v0.0.0",
        "Minecraft Alpha v1.1.2",
        "Minecraft Alpha v1.1.2_00",
        "Minecraft v1.20.1",
        "Minecraft Alpha v1.1.2_wakeup"
    };
    private static final ResourceKey<Level> BTD_DIMENSION = ResourceKey.create(
        Registries.DIMENSION,
        ResourceLocation.parse(MODID + ":beyond_the_dream")
    );

    private static String currentText = BASE_TEXT;
    private static int flashTicks = 0;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != BTD_DIMENSION) {
            currentText = BASE_TEXT;
            flashTicks = 0;
            return;
        }

        if (flashTicks > 0) {
            flashTicks--;
            if (flashTicks == 0) {
                currentText = BASE_TEXT;
            }
            /** Dynamic String Change
             * 6000 - ticks between each random "glitch" string
             * "//2-6 ticks" - "glitch" string show time
             */
        } else if (random.nextInt(6000) == 0) {
            currentText = EGGS[random.nextInt(EGGS.length)];
            flashTicks = 2 + random.nextInt(5); // 2–4 ticks
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.dimension() != BTD_DIMENSION) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack poseStack = guiGraphics.pose();
        int x = 3; // text distance from window border on X axis
        int y = 3; // text distance from window border on Y axis
        int textColor = 0xFFFFFF; // text color
        int textShadowColor = 0xFF3F3F3F; // text shadow color

        float scale = 0.8f; // Adjust this value to increase or decrease text size
        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0f);

        guiGraphics.drawString(mc.font, Component.literal(currentText), x + 1, y + 1, textShadowColor, false);
        guiGraphics.drawString(mc.font, Component.literal(currentText), x, y, textColor, false);

        poseStack.popPose();
    }
}
