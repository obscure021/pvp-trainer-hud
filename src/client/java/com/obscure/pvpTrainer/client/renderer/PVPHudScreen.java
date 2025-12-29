package com.obscure.pvpTrainer.client.renderer;

import com.obscure.pvpTrainer.client.config.ModConfig.LabelConfig;
import com.obscure.pvpTrainer.client.config.ModConfig.LabelPosition;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.CONFIG;

public final class PVPHudScreen
{
    // font
    public static final Minecraft CLIENT = Minecraft.getInstance();
    public static final Font FONT = CLIENT.font;
    private static final EnumMap<LabelPosition, Integer> LABELS_STACK_OFFSETS = new EnumMap<>(LabelPosition.class);
    // hotbar constants
    private static final int HOTBAR_WIDTH = 182;
    private static final int HOTBAR_HEIGHT = 22;
    private static final int HOTBAR_SLOT_WIDTH = HOTBAR_WIDTH / 9;
    private static final int HOTBAR_TEXT_PADDING = 3;
    // cache (for performance)
    // hotbar keybinds
    private static final String[] HOTBAR_KEYBINDS = new String[9];
    // screen size
    private static int screenW;
    private static int screenH;
    // pitch angle
    private static float lastPitch = Float.NaN;
    private static String cachedPitch = "";

    public static void clientStartInit()
    {
        refreshHotbarKeys();
    }

    public static void refreshHotbarKeys()
    {
        for (int i = 0; i < 9; i++)
        {
            HOTBAR_KEYBINDS[i] = CLIENT.options.keyHotbarSlots[i].getTranslatedKeyMessage().getString();
        }
    }

    public static void render(GuiGraphics context, DeltaTracker tickCounter, String lastKey)
    {
        // if disabled; RETURN
        if (!CONFIG.enableHud) return;

        // if F1 pressed; RETURN
        Minecraft client = Minecraft.getInstance();
        if (client.options.hideGui) return;

        // player null check
        Player player = client.player;
        if (player == null) return;

        // if creative mode and creative gui disabled; RETURN
        if (!CONFIG.showInCreative && player.isCreative()) return;

        // update screen size
        screenW = client.getWindow().getGuiScaledWidth();
        screenH = client.getWindow().getGuiScaledHeight();

        LABELS_STACK_OFFSETS.clear();

        Font font = client.font;

        // movement state
        String moveState = //
                player.isCrouching() ? "Sneaking ..." : // else
                        player.isSprinting() ? "Sprinting ..." : "";

        if (CONFIG.moveStateLabelConfig.enabled)
        {
            drawLabel(context, font, moveState, CONFIG.moveStateLabelConfig);
        }

        if (CONFIG.pressedKeyLabelConfig.enabled)
        {
            drawLabel(context, font, lastKey, CONFIG.pressedKeyLabelConfig);
        }

        if (CONFIG.pitchAngleLabelConfig.enabled)
        {
            drawLabel(context, font, getPitchText(player.getXRot()), CONFIG.pitchAngleLabelConfig);
        }

        // don't draw hotbar in spectator mode
        if (CONFIG.hotbar.showHotbarKeybinds && !player.isSpectator())
        {
            drawHotbar(context);
        }
    }

    private static String getPitchText(float pitch)
    {
        // round to 1 decimal place
        float rounded = Math.round(pitch * 10.0f) / 10.0f;
        // cache result string
        if (rounded != lastPitch)
        {
            lastPitch = rounded;
            cachedPitch = Float.toString(rounded);
        }
        return cachedPitch;
    }

    private static void drawLabel(GuiGraphics context, Font font, String text, LabelConfig labelConfig)
    {
        int offset = LABELS_STACK_OFFSETS.getOrDefault(labelConfig.position, 0);

        int fontWidth = font.width(text);
        int fontHeight = font.lineHeight;

        int xPos;
        int yPos;

        switch (labelConfig.position)
        {
            case TOP_LEFT ->
            {
                xPos = labelConfig.margin;
                yPos = labelConfig.margin + offset;
            }
            case TOP_RIGHT ->
            {
                xPos = screenW - fontWidth - (labelConfig.padding * 2) - labelConfig.margin;
                yPos = labelConfig.margin + offset;
            }
            case BOTTOM_LEFT ->
            {
                // - offset to stack upwards
                xPos = labelConfig.margin;
                yPos = screenH - fontHeight - (labelConfig.padding * 2) - labelConfig.margin - offset;
            }
            case BOTTOM_RIGHT ->
            {
                // - offset to stack upwards
                xPos = screenW - fontWidth - (labelConfig.padding * 2) - labelConfig.margin;
                yPos = screenH - fontHeight - (labelConfig.padding * 2) - labelConfig.margin - offset;
            }
            default -> throw new IllegalStateException();
        }

        PVPRendererUtils.drawTextAbsolute(
                context,
                text,
                xPos,
                yPos,
                labelConfig.backgroundColor,
                labelConfig.textColor,
                labelConfig.backgroundColorOpacity,
                labelConfig.padding,
                1.0f
        );

        LABELS_STACK_OFFSETS.put(labelConfig.position, offset + fontHeight + labelConfig.padding + labelConfig.topMargin);
    }

    private static void drawHotbar(GuiGraphics context)
    {
        int hotbarX = (screenW - HOTBAR_WIDTH) / 2;
        int hotbarY = screenH - HOTBAR_HEIGHT;

        for (int i = 0; i < 9; i++)
        {
            int baseX = hotbarX + (i * HOTBAR_SLOT_WIDTH) + HOTBAR_TEXT_PADDING;
            int baseY = hotbarY + HOTBAR_TEXT_PADDING;

            PVPRendererUtils.drawTextAbsolute(
                    context,
                    HOTBAR_KEYBINDS[i],
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
