package com.obscure.pvpTrainer.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class PVPHudRendererUtils {
    private static final Font FONT = Minecraft.getInstance().font;

    public static void drawText(GuiGraphics g, String text, int posX, int posY, int bgColor, int fgColor) {
        drawText(g, text, posX, posY, bgColor, fgColor, 100, 5, 1.0f);
    }

    public static void drawText(GuiGraphics g, String text, int posX, int posY, int bgColor, int fgColor, int bgOpacity) {
        drawText(g, text, posX, posY, bgColor, fgColor, bgOpacity, 5, 1.0f);
    }

    public static void drawText(GuiGraphics g, String text, int posX, int posY, int bgColor, int fgColor, int bgOpacity, int padding, float scale) {
        // Background 50% opaque
        bgColor = (bgOpacity << 24) | (bgColor & 0xFFFFFF);

        // ignore alpha value
        fgColor = (fgColor & 0xFFFFFF);


        if (text.isEmpty()) return;

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        int boxW = (int) ((textW * scale) + padding * 2);
        int boxH = (int) ((textH * scale) + padding * 2);

        int boxX1 = posX;
        int boxY1 = posY;
        int boxX2 = posX + boxW;
        int boxY2 = posY + boxH;

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
        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);

        int scaledX = (int) ((posX + padding) / scale);
        int scaledY = (int) ((posY + padding) / scale);

        g.drawString(FONT, text, scaledX, scaledY, fgColor, false);

        g.pose().popPose();
    }
}
