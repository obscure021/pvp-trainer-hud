package com.obscure.pvpTrainer.client.renderer;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.CONFIG;

public class PVPHudScreen
{
    public static int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    public static int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    public static void render(GuiGraphics context, DeltaTracker tickCounter, String lastKey)
    {
        if (!CONFIG.enableHud) return;

        Minecraft client = Minecraft.getInstance();

        // return if no gui
        if (client.options.hideGui) return;

        // return if no player
        if (client.player == null) return;

        // return if in creative mode and config says so
        if (!CONFIG.showInCreative && client.player.isCreative()) return;

        // refresh width and height
        screenW = client.getWindow().getGuiScaledWidth();
        screenH = client.getWindow().getGuiScaledHeight();

        String moveState = ( //
                client.player.isSprinting() ? "Sprinting ..." : //
                        (client.player.isCrouching() ? "Sneaking ..." : "") //
        );

        if (CONFIG.moveStateLabel.enabled)
        {
            PVPRendererUtils.drawTextRelative(
                    context,
                    moveState,
                    CONFIG.moveStateLabel.xPositionPercent,
                    CONFIG.moveStateLabel.yPositionPercent,
                    CONFIG.moveStateLabel.backgroundColor,
                    CONFIG.moveStateLabel.textColor,
                    CONFIG.moveStateLabel.backgroundColorOpacity
            );
        }

        if (CONFIG.pressedKeyLabel.enabled)
        {
            PVPRendererUtils.drawTextRelative(
                    context,
                    lastKey,
                    CONFIG.pressedKeyLabel.xPositionPercent,
                    CONFIG.pressedKeyLabel.yPositionPercent,
                    CONFIG.pressedKeyLabel.backgroundColor,
                    CONFIG.pressedKeyLabel.textColor,
                    CONFIG.pressedKeyLabel.backgroundColorOpacity
            );
        }

        // show hotbar binds if not in spectator mode
        if (CONFIG.hotbar.showHotbarKeybinds && !client.player.isSpectator())
        {
            drawHotbar(context, client);
        }

        // show angle
        if (CONFIG.pitchAngle.enabled)
        {
            String angleText = String.valueOf(client.player.getXRot());
            PVPRendererUtils.drawTextRelative(
                    context,
                    angleText,
                    CONFIG.pitchAngle.xPositionPercent,
                    CONFIG.pitchAngle.yPositionPercent,
                    CONFIG.pitchAngle.backgroundColor,
                    CONFIG.pitchAngle.textColor,
                    CONFIG.pitchAngle.backgroundColorOpacity
            );
        }
    }

    private static void drawHotbar(GuiGraphics context, Minecraft client)
    {
        // predefined
        final int hotbarWidth = 182;
        final int hotbarHeight = 22;
        final int slotWidth = hotbarWidth / 9;

        final int hotbarX = (PVPHudScreen.screenW - hotbarWidth) / 2;
        final int hotbarY = PVPHudScreen.screenH - hotbarHeight;

        // hotbar text padding
        final int textPadding = 3;

        // loop through all 9 hotbar keybinds
        for (int i = 0; i < 9; i++)
        {
            String key = client.options.keyHotbarSlots[i].getTranslatedKeyMessage().getString();

            int baseX = hotbarX + (i * slotWidth) + textPadding;
            int baseY = hotbarY + textPadding;

            PVPRendererUtils.drawTextAbsolute(
                    context,
                    key,
                    baseX,
                    baseY,
                    CONFIG.hotbar.backgroundColor,
                    CONFIG.hotbar.textColor,
                    CONFIG.hotbar.backgroundColorOpacity,
                    2,
                    0.7f
            );
        }
    }
}