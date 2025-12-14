package com.obscure.pvpTrainer.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {

    public boolean enableHud = true;

    @ConfigEntry.Gui.CollapsibleObject
    public Label sprintLabel = new Label(10, 10);

    @ConfigEntry.Gui.CollapsibleObject
    public Label pressedKeyLabel = new Label(10, 35);
    public boolean detectMouseButtons = true;

    @ConfigEntry.Gui.CollapsibleObject
    public Hotbar hotbar = new Hotbar();

    public static class Label {
        public boolean enabled = true;
        public int xPosition;
        public int yPosition;
        @ConfigEntry.ColorPicker
        public int textColor = 0xFFFFFF;
        @ConfigEntry.ColorPicker
        public int backgroundColor = 0x000000;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int backgroundColorOpacity = 128;

        Label(int xPosition, int yPosition) {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
        }

        Label() {
        }
    }

    public static class Hotbar {
        public boolean showHotbarKeybinds = true;
        @ConfigEntry.ColorPicker
        public int textColor = 0xFFFFFF;
        @ConfigEntry.ColorPicker
        public int backgroundColor = 0x000000;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int backgroundColorOpacity = 128;
    }
}