package ru.whispershadow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class WhisperShadowClient implements ClientModInitializer {
    public static final String MOD_ID = "whispershadow";

    @Override
    public void onInitializeClient() {
        ModSounds.init();
        HorrorManager.init();
        ClientTickEvents.END_CLIENT_TICK.register(HorrorManager::tick);
        ClientSendMessageEvents.CHAT.register(HorrorManager::handlePlayerChat);
        HudRenderCallback.EVENT.register(HorrorManager::renderOverlay);
    }
}
