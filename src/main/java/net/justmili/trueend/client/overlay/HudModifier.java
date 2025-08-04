package net.justmili.trueend.client.overlay;

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
        assert player != null;

        if (!player.level().dimension().equals(BTD)) return;

        NamedGuiOverlay overlay = event.getOverlay();
        ResourceLocation id = overlay.id();

        boolean jumpBarActive;
        if (player.getVehicle() instanceof AbstractHorse horse && horse.isSaddled()) { jumpBarActive = true; } else { jumpBarActive = false; }
        int yOffset = jumpBarActive ? 6 : 0;

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        if (id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            event.setCanceled(true);
        }
        if (id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            event.setCanceled(true);
        }

        if (id.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
            event.setCanceled(true);

            IGuiOverlay armorOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            armorOverlay.render((ForgeGui) mc.gui, gui, pt, w + 200, h + 16 - yOffset);
        }
        if (id.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            event.setCanceled(true);

            IGuiOverlay healthOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            healthOverlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6 - yOffset);
        }
        if (id.equals(VanillaGuiOverlay.AIR_LEVEL.id())) {
            event.setCanceled(true);

            if (player.getArmorValue() > 0) {
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 4 - yOffset);
            } else {
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6 - yOffset);
            }
        }
        if (id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
            event.setCanceled(true);

            IGuiOverlay mountHpOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            mountHpOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 5 - yOffset);
        }
    }
}
