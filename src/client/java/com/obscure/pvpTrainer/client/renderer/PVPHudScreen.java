package com.obscure.pvpTrainer.client.renderer;

import com.obscure.pvpTrainer.client.config.ModConfig.Label;
import com.obscure.pvpTrainer.client.config.ModConfig.LabelPosition;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.EnumMap;
import java.util.Map;

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

        Map<LabelPosition, Integer> stackOffsets = new EnumMap<>(LabelPosition.class);

        String moveState = //
                client.player.isSprinting() ? "Sprinting ..." : //
                        (client.player.isCrouching() ? "Sneaking ..." : ""); //

        if (CONFIG.moveStateLabel.enabled && !moveState.isEmpty())
        {
            drawLabel(context, moveState, CONFIG.moveStateLabel, screenW, screenH, stackOffsets);
        }

        if (CONFIG.pressedKeyLabel.enabled && lastKey != null && !lastKey.isEmpty())
        {
            drawLabel(context, lastKey, CONFIG.pressedKeyLabel, screenW, screenH, stackOffsets);
        }

        // show angle
        if (CONFIG.pitchAngleLabel.enabled)
        {
            String angleText = String.valueOf(client.player.getXRot());
            drawLabel(context, angleText, CONFIG.pitchAngleLabel, screenW, screenH, stackOffsets);
        }

        // show hotbar binds if not in spectator mode
        if (CONFIG.hotbar.showHotbarKeybinds && !client.player.isSpectator())
        {
            drawHotbar(context, client);
        }
    }

    private static void drawLabel(GuiGraphics context, String text, Label label, int screenW, int screenH, Map<LabelPosition, Integer> offsetsY)
    {
        int offset = offsetsY.getOrDefault(label.position, 0);

        int x;
        int y;

        int fontWidth = Minecraft.getInstance().font.width(text);
        int fontHeight = Minecraft.getInstance().font.lineHeight;

        switch (label.position)
        {
            case TOP_LEFT ->
            {
                x = label.margin;
                y = label.margin + offset;
            }
            case TOP_RIGHT ->
            {
                x = screenW - fontWidth - (label.padding * 2) - label.margin;
                y = label.margin + offset;
            }
            case BOTTOM_LEFT ->
            {
                x = label.margin;
                y = screenH - fontHeight - (label.padding * 2) - label.margin - offset;
            }
            case BOTTOM_RIGHT ->
            {
                x = screenW - fontWidth - (label.padding * 2) - label.margin;
                y = screenH - fontHeight - (label.padding * 2) - label.margin - offset;
            }
            default -> throw new IllegalStateException();
        }

        PVPRendererUtils.drawTextAbsolute(
                context,
                text,
                x,
                y,
                label.backgroundColor,
                label.textColor,
                label.backgroundColorOpacity,
                label.padding,
                1.0f
        );
        offsetsY.put(label.position, offset + fontHeight + label.padding + label.topMargin);
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