package com.obscure.pvpTrainer.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;


public class PVPHudRendererUtils {
    private static final Font FONT = Minecraft.getInstance().font;

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor) {
        drawTextRelative(g, text, posXPercent, posYPercent, bgColor, fgColor, 100, 5, 1.0f);
    }

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor, int bgOpacity) {
        drawTextRelative(g, text, posXPercent, posYPercent, bgColor, fgColor, bgOpacity, 5, 1.0f);
    }

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor, int bgOpacity, int padding, float scale) {
        int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        int boxW = (int) ((textW * scale) + padding * 2);
        int boxH = (int) ((textH * scale) + padding * 2);

        // convert relative to absolute
        float posX = (posXPercent * 0.01f) * (screenW - boxW);
        float posY = (posYPercent * 0.01f) * (screenH - boxH);

        drawTextAbsolute(g, text, (int) posX, (int) posY, bgColor, fgColor, bgOpacity, padding, scale);
    }

    public static void drawTextAbsolute(GuiGraphics g, String text, int posX, int posY, int bgColor, int fgColor, int bgOpacity, int padding, float scale) {
        // Background 50% opaque
        bgColor = (bgOpacity << 24) | (bgColor & 0xFFFFFF);

        // ignore alpha value
        fgColor = (fgColor & 0xFFFFFF);
        // make fully opaque
        fgColor = (0xFF << 24) | fgColor;

        if (text.isEmpty()) return;

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        int boxW = (int) ((textW * scale) + padding * 2);
        int boxH = (int) ((textH * scale) + padding * 2);

        int boxX1 = (int) (posX);
        int boxY1 = (int) (posY);
        int boxX2 = (int) (posX + boxW);
        int boxY2 = (int) (posY + boxH);

        // Fill main rectangle body
        g.fill(boxX1 + 1, boxY1 + 1, boxX2 - 1, boxY2 - 1, bgColor);

        // Fill pixel “rounded” corners
        g.fill(boxX1, boxY1 + 1, boxX1 + 1, boxY2 - 1, bgColor); // left strip
        g.fill(boxX2 - 1, boxY1 + 1, boxX2, boxY2 - 1, bgColor); // right strip
        g.fill(boxX1 + 1, boxY1, boxX2 - 1, boxY1 + 1, bgColor); // top strip
        g.fill(boxX1 + 1, boxY2 - 1, boxX2 - 1, boxY2, bgColor); // bottom strip

        // Optionally cut 1 pixel at corners for “rounded” look
        g.fill(boxX1, boxY1, boxX1 + 1, boxY1 + 1, 0x00000000); // top-left
        g.fill(boxX2 - 1, boxY1, boxX2, boxY1 + 1, 0x00000000); // top-right
        g.fill(boxX1, boxY2 - 1, boxX1 + 1, boxY2, 0x00000000); // bottom-left
        g.fill(boxX2 - 1, boxY2 - 1, boxX2, boxY2, 0x00000000); // bottom-right

        // Draw scaled text
        g.pose().pushMatrix();
        g.pose().scale(scale, scale, g.pose());

        int scaledX = (int) ((posX + padding) / scale);
        int scaledY = (int) ((posY + padding) / scale);

        g.drawString(FONT, text, scaledX, scaledY, fgColor, false);

        g.pose().popMatrix();
    }

}
