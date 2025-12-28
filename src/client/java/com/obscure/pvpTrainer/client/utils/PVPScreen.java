package com.obscure.pvpTrainer.client.utils;

import com.mojang.blaze3d.platform.InputConstants;
import com.obscure.pvpTrainer.client.renderer.PVPHudScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import static com.obscure.pvpTrainer.client.PvpTrainerClient.CONFIG;
import static com.obscure.pvpTrainer.client.PvpTrainerClient.MOD_ID;

public class PVPScreen {
    public static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "pvp-trainer-hud");
    private static String lastKey;

    public static void init() {
        // poll current key once every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window = client.getWindow().handle();
            refreshLastKey(window);
        });

    }


    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PVPHudScreen.render(guiGraphics, deltaTracker, lastKey);
    }

    private static void refreshLastKey(long window) {
        // Keyboard Buttons
        for (int key = GLFW.GLFW_KEY_SPACE; key <= GLFW.GLFW_KEY_LAST; key++) {
            if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                InputConstants.Key mcKey = InputConstants.Type.KEYSYM.getOrCreate(key);
                lastKey = mcKey.getDisplayName().getString();
                return;
            }
        }

        // Check mouse buttons
        if (CONFIG.detectMouseButtons) {
            for (int button = GLFW.GLFW_MOUSE_BUTTON_1; button <= GLFW.GLFW_MOUSE_BUTTON_LAST; button++) {
                if (GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS) {
                    lastKey = "Mouse " + button;
                    return;
                }
            }
        }

        lastKey = "";
    }
}
