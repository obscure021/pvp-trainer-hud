package com.obscure.pvpTrainer.client.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.CONFIG;

public class PVPHudRenderer {
    public static int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    public static int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    public static void render(GuiGraphics context, DeltaTracker tickCounter, String lastKey) {
        if (!CONFIG.enableHud) return;

        Minecraft client = Minecraft.getInstance();

        // return if no player
        if (client.player == null) return;

        // refresh width and height
        screenW = client.getWindow().getGuiScaledWidth();
        screenH = client.getWindow().getGuiScaledHeight();

        PVPHudRendererUtils.drawTextRelative(
                context, (client.player.isSprinting() ? "Sprinting ..." : ""),
                CONFIG.sprintLabel.xPositionPercent,
                CONFIG.sprintLabel.yPositionPercent,
                CONFIG.sprintLabel.backgroundColor,
                CONFIG.sprintLabel.textColor,
                CONFIG.sprintLabel.backgroundColorOpacity
        );

        PVPHudRendererUtils.drawTextRelative(
                context, lastKey,
                CONFIG.pressedKeyLabel.xPositionPercent,
                CONFIG.pressedKeyLabel.yPositionPercent,
                CONFIG.pressedKeyLabel.backgroundColor,
                CONFIG.pressedKeyLabel.textColor,
                CONFIG.pressedKeyLabel.backgroundColorOpacity
        );

        if (CONFIG.hotbar.showHotbarKeybinds) {
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

            PVPHudRendererUtils.drawTextAbsolute(
                    context, key, baseX, baseY,
                    CONFIG.hotbar.backgroundColor,
                    CONFIG.hotbar.textColor,
                    CONFIG.hotbar.backgroundColorOpacity,
                    2, 0.7f
            );
        }
    }
}