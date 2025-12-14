package com.obscure.pvpTrainer.client.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.CONFIG;


public class PVPHudRenderer {
    static int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    static int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    public static void render(GuiGraphics context, DeltaTracker tickCounter, String lastKey) {
        if (!CONFIG.enableHud) return;

        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        // return if no player
        if (player == null) return;

        // refresh width and height
        screenW = client.getWindow().getGuiScaledWidth();
        screenH = client.getWindow().getGuiScaledHeight();

        PVPHudRendererUtils.drawText(context, (player.isSprinting() ? "Sprinting ..." : ""), CONFIG.sprintLabel.xPosition, CONFIG.sprintLabel.yPosition, CONFIG.sprintLabel.backgroundColor, CONFIG.sprintLabel.textColor);
        PVPHudRendererUtils.drawText(context, lastKey, CONFIG.pressedKeyLabel.xPosition, CONFIG.pressedKeyLabel.yPosition, CONFIG.pressedKeyLabel.backgroundColor, CONFIG.pressedKeyLabel.textColor);

        if (CONFIG.showHotbarOverlay) {
            drawHotbar(context, client);
        }
    }

    private static void drawHotbar(GuiGraphics context, Minecraft client) {
        // predefined
        final int hotbarWidth = 182;
        final int hotbarHeight = 22;
        final int slotWidth = hotbarWidth / 9;

        final int hotbarX = (PVPHudRenderer.screenW - hotbarWidth) / 2;
        final int hotbarY = PVPHudRenderer.screenH - hotbarHeight;

        // hotbar text padding
        final int textPadding = 3;

        // loop through all 9 hotbar keybinds
        for (int i = 0; i < 9; i++) {
            String key = client.options.keyHotbarSlots[i].getTranslatedKeyMessage().getString();

            int baseX = hotbarX + (i * slotWidth) + textPadding;
            int baseY = hotbarY + textPadding;

            PVPHudRendererUtils.drawText(context, key, baseX, baseY, CONFIG.hotbar.hotbarBackgroundColor, CONFIG.hotbar.hotbarTextColor, 2, 0.7f);
        }
    }
}