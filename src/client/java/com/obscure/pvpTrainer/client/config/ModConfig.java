package com.obscure.pvpTrainer.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData
{
    public boolean enableHud = true;
    public boolean showInCreative = false;
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public LabelConfig pitchAngleLabelConfig = new LabelConfig(LabelPosition.TOP_RIGHT, 0);
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public LabelConfig moveStateLabelConfig = new LabelConfig(LabelPosition.TOP_LEFT, 128);
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public LabelConfig pressedKeyLabelConfig = new LabelConfig(LabelPosition.TOP_LEFT, 128);
    public boolean detectMouseButtons = true;
    @ConfigEntry.Gui.CollapsibleObject
    public Hotbar hotbar = new Hotbar();

    public enum LabelPosition
    {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    public static class LabelConfig
    {
        public boolean enabled = true;
        public LabelPosition position;
        @ConfigEntry.ColorPicker
        public int textColor = 0xFFFFFF;
        @ConfigEntry.ColorPicker
        public int backgroundColor = 0x000000;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int backgroundColorOpacity;
        public int padding = 5;
        public int margin = 6;
        public int topMargin = 12;

        LabelConfig(LabelPosition position, int backgroundColorOpacity)
        {
            this.position = position;
            this.backgroundColorOpacity = backgroundColorOpacity;
        }
    }

    public static class Hotbar
    {
        public boolean showHotbarKeybinds = true;
        @ConfigEntry.ColorPicker
        public int textColor = 0xFFFFFF;
        @ConfigEntry.ColorPicker
        public int backgroundColor = 0x000000;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int backgroundColorOpacity = 128;
    }
}