package net.justmili.trueend.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TrueEndCreditsScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrueEndCreditsScreen.class);
    private static final ResourceLocation TITLE_TEX = ResourceLocation.parse("true_end:textures/gui/title.png");
    private static final ResourceLocation TEXT_FILE  = ResourceLocation.parse("true_end:texts/credits.txt");

    private final Runnable onClose;
    private final List<String> lines = new ArrayList<>();
    private float scroll = 0f;

    /** Default: closes back to game when any key/Esc is pressed */
    public TrueEndCreditsScreen() {
        this(() -> Minecraft.getInstance().setScreen(null));
    }

    /** Allows custom onClose behavior */
    public TrueEndCreditsScreen(Runnable onClose) {
        super(Component.empty());
        this.onClose = onClose;
        loadCreditsText();
    }

    private void loadCreditsText() {
        try (var stream = Minecraft.getInstance()
                .getResourceManager()
                .open(TEXT_FILE);
             var br     = new BufferedReader(new InputStreamReader(stream)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.replace("PLAYERNAME",
                        Minecraft.getInstance()
                                .player
                                .getName()
                                .getString()));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load credits.txt", e);
        }
    }

    @Override
    public void renderBackground(GuiGraphics gui) {
        super.renderBackground(gui);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gui);

        // Title
        RenderSystem.setShaderTexture(0, TITLE_TEX);
        int texW = 256, texH = 64;
        int x = (this.width - texW) / 2, y = 20;
        gui.blit(TITLE_TEX, x, y, 0, 0, texW, texH, texW, texH);

        // Scrolling text
        Font font = this.font;
        int startY = 100 - (int) scroll;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            int tx = (this.width - font.width(s)) / 2;
            int ty = startY + i * 12;
            gui.drawString(font, s, tx, ty, 0xFFFFFF, true);
        }

        scroll += partialTicks * 0.5f;
        super.render(gui, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        onClose.run();
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        onClose.run();
        return true;
    }
}
