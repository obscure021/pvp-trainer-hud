package com.obscure.pvpTrainer.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.obscure.pvpTrainer.client.config.ModConfig;
import com.obscure.pvpTrainer.client.renderer.PVPHudRenderer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import org.lwjgl.glfw.GLFW;

public class PvpTrainerClient implements ClientModInitializer {
    public static final String MOD_ID = "pvp-trainer";
    private static final ResourceLocation HUD_LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "pvp-trainer-hud");
    public static ModConfig CONFIG;
    private static String lastKey;

    private static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PVPHudRenderer.render(guiGraphics, deltaTracker, lastKey);
    }

    @Override
    public void onInitializeClient() {
        // init config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((holder, newConfig) -> {
            CONFIG = newConfig;
            return InteractionResult.PASS;
        });

        // poll current key once every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window = client.getWindow().handle();

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
                        return; // stop after first pressed mouse button
                    }
                }
            }

            lastKey = "";
        });

        HudElementRegistry.addLast(HUD_LAYER, PvpTrainerClient::render);
    }
}
