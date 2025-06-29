package net.justmili.trueend.client.gui.renderers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.systems.RenderSystem;

public class BlackOverlay extends AbstractContainerScreen<net.justmili.trueend.client.gui.inventory.BlackOverlay> {

    public BlackOverlay(net.justmili.trueend.client.gui.inventory.BlackOverlay container, Inventory inventory, Component text) {
        super(container, inventory, text);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
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
        RenderSystem.setShaderTexture(0, ResourceLocation.parse("true_end:textures/screens/funny.png"));

        final int texW = 1280;
        final int texH = 720;
        int winW = this.width;
        int winH = this.height;
        float scaleX = (float) winW / texW;
        float scaleY = (float) winH / texH;

        var pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(scaleX, scaleY, 1f);

        guiGraphics.blit(
                ResourceLocation.parse("true_end:textures/screens/black_screen.png"),
                0, 0,
                0, 0,
                texW, texH,
                texW, texH
        );
        pose.popPose();

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
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
