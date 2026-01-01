package com.obscure.pvpTrainer.client;

import com.obscure.pvpTrainer.client.config.ModConfig;
import com.obscure.pvpTrainer.client.utils.PVPScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PvpTrainerClient implements ClientModInitializer
{
    public static final String MOD_ID = "pvp-trainer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // render layer
    public static final ResourceLocation RENDER_LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "pvp-trainer-layer");

    // config
    public static ModConfig CONFIG;

    private static void renderHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        PVPScreen.render(guiGraphics, deltaTracker);
    }

    @Override
    public void onInitializeClient()
    {
        // init config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((holder, newConfig) -> {
            CONFIG = newConfig;
            return InteractionResult.PASS;
        });

        PVPScreen.init();

        // for 1.21.6 +
        // HudElementRegistry.addLast(RENDER_LAYER, PvpTrainerClient::renderHud);
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(
                IdentifiedLayer.CHAT,
                RENDER_LAYER,
                PvpTrainerClient::renderHud
        ));
    }
}
