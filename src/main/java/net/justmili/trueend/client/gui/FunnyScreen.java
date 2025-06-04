package net.justmili.trueend.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.justmili.trueend.world.inventory.FunnyMenu;

import java.util.HashMap;

public class FunnyScreen extends AbstractContainerScreen<FunnyMenu> {
    private final static HashMap<String, Object> guistate = FunnyMenu.guistate;
    private final Level world;
    private final int x, y, z;
    private final Player entity;

    // Point this at your 1920×1080 image
    private static final ResourceLocation texture =
            ResourceLocation.parse("true_end:textures/screens/funny.png");

    public FunnyScreen(FunnyMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world  = container.world;
        this.x      = container.x;
        this.y      = container.y;
        this.z      = container.z;
        this.entity = container.entity;

        // Set the “GUI size” to match your full‐HD image
        this.imageWidth  = 720;
        this.imageHeight = 405;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Compute offsets so that a 1920×1080 texture is centered
        int xCenter = (this.width  - this.imageWidth)  / 2;
        int yCenter = (this.height - this.imageHeight) / 2;

        // Draw the full 1920×1080 texture at (xCenter, yCenter).
        guiGraphics.blit(
                texture,
                xCenter,                  // x on screen
                yCenter,                  // y on screen
                0,                        // u (texture‐coords)
                0,                        // v (texture‐coords)
                this.imageWidth,          // width to draw (1920)
                this.imageHeight,         // height to draw (1080)
                this.imageWidth,          // texture sheet width (1920)
                this.imageHeight          // texture sheet height (1080)
        );

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        // Escape closes the GUI
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    public void containerTick() {
        super.containerTick();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // No labels on top of the image
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
    }
}
