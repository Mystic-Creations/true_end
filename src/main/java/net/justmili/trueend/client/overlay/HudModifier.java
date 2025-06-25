package net.justmili.trueend.client.overlay;

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

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT)
public class HudModifier {

    //This has to be here
    private static boolean jumpBarActive = false;

    @SubscribeEvent
    public static void onGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;

        //Check if player is in BTD
        if (!player.level().dimension().equals(BTD)) return;

        NamedGuiOverlay overlay = event.getOverlay();
        ResourceLocation id = overlay.id();

        //Horse jump bar stuff
        if (player.getVehicle() instanceof AbstractHorse horse && horse.isSaddled()) {
            jumpBarActive = true;
        } else {
            jumpBarActive = false;
        }
        int yOffset = jumpBarActive ? 6 : 0;

        //Vars
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        //Hide elements
        if (id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            event.setCanceled(true);
        }
        if (id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            event.setCanceled(true);
        }

        //Move armor bar, player health and air level
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
                //if armor bar is rendered
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 4 - yOffset);
            } else {
                //if armor bar is not rendered
                IGuiOverlay airOverlay = overlay.overlay();
                GuiGraphics gui = event.getGuiGraphics();
                float pt = event.getPartialTick();
                airOverlay.render((ForgeGui) mc.gui, gui, pt, w, h + 6 - yOffset);
            }
        }

        //Move mount health
        if (id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())) {
            event.setCanceled(true);

            IGuiOverlay mountHpOverlay = overlay.overlay();
            GuiGraphics gui = event.getGuiGraphics();
            float pt = event.getPartialTick();
            mountHpOverlay.render((ForgeGui) mc.gui, gui, pt, w, h - 5 - yOffset);
        }
    }
}
