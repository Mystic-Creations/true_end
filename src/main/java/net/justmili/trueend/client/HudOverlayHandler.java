package net.justmili.trueend.client;

import net.justmili.trueend.TrueEnd;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT)
public class HudOverlayHandler {

    @SubscribeEvent
    public static void onGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // Only run in Beyond The Dream (BTD)
        if (!player.level().dimension().equals(BTD)) return;

        NamedGuiOverlay overlay = event.getOverlay();
        ResourceLocation id = overlay.id();

        if (id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            event.setCanceled(true);
        }
        if (id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            event.setCanceled(true);
        }

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        if (id.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
            event.setCanceled(true);

            IGuiOverlay armorOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            armorOverlay.render((ForgeGui) mc.gui, gui, pt, w + 200, h + 16);
        }
        if (id.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            event.setCanceled(true);

            IGuiOverlay healthOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            healthOverlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6);
        }
        if (id.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
            event.setCanceled(true);

            if (player.getArmorValue() > 0) {
                //if armor bar is rendered
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 4);
            } else {
                //if armor bar is not rendered
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6);
            }
        }

        if (id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
            event.setCanceled(true);

            IGuiOverlay mountHpOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            mountHpOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 5);
        }
        if (id.equals(VanillaGuiOverlay.JUMP_BAR.id())) {
            event.setCanceled(true);
        }
    }
}
