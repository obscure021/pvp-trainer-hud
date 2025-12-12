package com.obscure.pvpTrainer.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class PvpTrainerClient implements ClientModInitializer {
    public static final String MOD_ID = "pvp-trainer";
    private static final ResourceLocation HUD_LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "pvp-trainer-hud");

    private static String lastKey;

    private static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PVPHudRenderer.render(guiGraphics, deltaTracker, lastKey);
    }

    @Override
    public void onInitializeClient() {
        // poll current key once every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window = client.getWindow().getWindow();

            for (int key = GLFW.GLFW_KEY_SPACE; key <= GLFW.GLFW_KEY_LAST; key++) {
                if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                    InputConstants.Key mcKey = InputConstants.getKey(key, -1);
                    lastKey = mcKey.getDisplayName().getString();
                    return;
                }
            }

            lastKey = "";
        });

        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, HUD_LAYER, PvpTrainerClient::render));
    }
}
