package net.justmili.trueend.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class CreditsScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditsScreen.class);
    private static final ResourceLocation TITLE_TEX = ResourceLocation.parse("true_end:textures/gui/title.png");
    private static final ResourceLocation TEXT_FILE = ResourceLocation.parse("true_end:texts/credits.txt");
    private static final ResourceLocation BG_TEXTURE = ResourceLocation.parse("true_end:textures/block/old_dirt.png");

    private final Runnable onClose;
    private final List<String> lines = new ArrayList<>();
    private float scroll = 0f;

    public CreditsScreen() {
        this(() -> {
            Minecraft.getInstance().getSoundManager().stop();
            Minecraft.getInstance().setScreen(null);
        });
    }

    public CreditsScreen(Runnable onClose) {
        super(Component.empty());
        this.onClose = onClose;
        loadCreditsText();
        this.scroll = 0f;
    }

    private void loadCreditsText() {
        try (var stream = Minecraft.getInstance().getResourceManager().open(TEXT_FILE);
                var br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                assert Minecraft.getInstance().player != null;
                lines.add(line.replace("PLAYERNAME",
                        Minecraft.getInstance().player.getName().getString()));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load credits.txt", e);
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics gui) {
        // Tile the dirt texture to fill the screen
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int texSize = 48;
        for (int x = 0; x < width; x += texSize) {
            for (int y = 0; y < height; y += texSize) {
                gui.blit(BG_TEXTURE, x, y, 0, 0, texSize, texSize, texSize, texSize);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gui);

        gui.fill(0, 0, width, height, 0x88000000);

        float scrollSpeed = 20f;
        scroll += scrollSpeed * (partialTicks / 20f);

        RenderSystem.setShaderTexture(0, TITLE_TEX);
        int texW = 256, texH = 64;
        float titleX = (width - texW) / 2f;
        float titleY = height - scroll;
        gui.blit(TITLE_TEX, (int) titleX, (int) titleY, 0, 0, texW, texH, texW, texH);

        Font font = this.font;
        float startY = titleY + texH + 20;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            int w = font.width(s);
            gui.drawString(font, s, (width - w) / 2, (int) (startY + i * 12), 0xFFFFFF, true);
        }

        super.render(gui, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW_KEY_ESCAPE) {
            onClose.run();
            return true;
        }
        return false;
    }
}