package net.justmili.trueend.client.overlay;

import mod.adrenix.nostalgic.tweak.config.CandyTweak;
import net.justmili.trueend.TrueEnd;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HudModifier {
    @SubscribeEvent
    public static void onGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        NamedGuiOverlay getOverlay = event.getOverlay();
        IGuiOverlay overlay = getOverlay.overlay();
        ResourceLocation id = getOverlay.id();
        GuiGraphics gui = event.getGuiGraphics();
        float pt = event.getPartialTick();

        boolean inBTD = player.level().dimension().equals(BTD);

        if (inBTD) {
            boolean jumpBarActive;
            if (player.getVehicle() instanceof AbstractHorse horse && horse.isSaddled()) {
                jumpBarActive = true;
            } else {
                jumpBarActive = false;
            }
            int horseOffset = jumpBarActive ? 6 : 0;
            int fullscreenOffset = 1; //kinda inefficient, but at least we know what this does
            int yOffset = horseOffset - fullscreenOffset;

            int w = mc.getWindow().getGuiScaledWidth();
            int h = mc.getWindow().getGuiScaledHeight();

            if (id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) event.setCanceled(true);
            if (id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) event.setCanceled(true);
            if (id.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w + 200, h + 16 - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6 - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w - 202, h - 3 - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w, h - 5 - yOffset);
            }
        }
        if (TrueEnd.inModList("nostalgic_tweaks")) {
            if (inBTD) {
                String ns = id.getNamespace();
                String path = id.getPath().toLowerCase();
                if (!("nostalgic_tweaks".equals(ns))) return;
                if (path.contains("stamina")) event.setCanceled(true);
                if (CandyTweak.OLD_VERSION_OVERLAY.get()) CandyTweak.OLD_VERSION_OVERLAY.setCacheAndDiskThenSave(false);
            } else {
                if (!CandyTweak.OLD_VERSION_OVERLAY.get()) CandyTweak.OLD_VERSION_OVERLAY.setCacheAndDiskThenSave(true);
            }
        }
    }
}
