package net.mysticcreations.true_end.client.overlay;

import mod.adrenix.nostalgic.tweak.config.CandyTweak;
import net.minecraft.world.entity.Entity;
import net.mysticcreations.true_end.TrueEnd;
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

import static net.mysticcreations.true_end.init.Dimensions.BTD;

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
            int w = mc.getWindow().getGuiScaledWidth();
            int h = mc.getWindow().getGuiScaledHeight();
            int fullscreenOffset;
            int horseBar;
            int armorW;
            int armorH;
            int playerHpH;
            int airLvlW;
            int airLvlH;
            int mountHpH;

            if (!TrueEnd.inModList("kilt")) {
                fullscreenOffset = 1;
                horseBar = 7;
                armorW = 200;
                armorH = 16;
                playerHpH = 6;
                airLvlW = 202;
                airLvlH = 3;
                mountHpH = 5;
            } else {
                fullscreenOffset = 1;
                horseBar = 7;
                armorW = 202;
                armorH = 6;
                playerHpH = 16;
                airLvlW = 202;
                airLvlH = 3;
                mountHpH = 3;
            }
            // TODO: BRING DOWN THE RENDERING ON UNSADDLED ABSTRACTHORSE AND OTHER MOUNTS
            Entity mount = player.getVehicle();
            int horseBarOffset = 0;
            if (mount instanceof AbstractHorse horse) {
                horseBarOffset = horse.isSaddled() ? horseBar : 0;
            }
            int yOffset = horseBarOffset - fullscreenOffset;
            int mountHpOffset = !TrueEnd.inModList("kilt") ? (horseBarOffset*2)-3 : 0;

            if (id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) event.setCanceled(true);
            if (id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) event.setCanceled(true);
            if (id.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w + armorW, h + armorH - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w, h + playerHpH - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w - airLvlW, h - airLvlH - yOffset);
            }
            if (id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
                event.setCanceled(true);
                overlay.render((ForgeGui) mc.gui, gui, pt, w, h - mountHpH - yOffset + mountHpOffset);
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
