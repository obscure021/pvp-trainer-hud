package com.obscure.pvpTrainer.client;

import com.obscure.pvpTrainer.client.config.ModConfig;
import com.obscure.pvpTrainer.client.utils.PVPScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PvpTrainerClient implements ClientModInitializer {
    public static final String MOD_ID = "pvp-trainer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ModConfig CONFIG;
    public static int CURRENT_HOTBAR_SLOT = 0;

    @Override
    public void onInitializeClient() {
        // init config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((holder, newConfig) -> {
            CONFIG = newConfig;
            return InteractionResult.PASS;
        });

        PVPScreen.init();

        HudElementRegistry.addLast(PVPScreen.LAYER, PVPScreen::render);
    }
}
