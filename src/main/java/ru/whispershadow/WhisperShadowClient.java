package ru.whispershadow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class WhisperShadowClient implements ClientModInitializer {

    public static final String MOD_ID = "whispershadow";

    @Override
    public void onInitializeClient() {

        ModSounds.init();

        HorrorManager.init();

        ClientTickEvents.END_CLIENT_TICK.register(
                HorrorManager::tick
        );

        ClientSendMessageEvents.CHAT.register(
                HorrorManager::handlePlayerChat
        );

        HudRenderCallback.EVENT.register(
                HorrorManager::renderOverlay
        );

        // ========================================
        // TEST COMMANDS
        // ========================================

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {

                    dispatcher.register(
                            ClientCommandManager.literal("ws")

                                    .then(
                                            ClientCommandManager.literal("figure")
                                                    .executes(context -> {

                                                        HorrorManager.spawnFigure(
                                                                context.getSource().getClient()
                                                        );

                                                        return 1;
                                                    })
                                    )

                                    .then(
                                            ClientCommandManager.literal("run")
                                                    .executes(context -> {

                                                        HorrorManager.fireRun(
                                                                context.getSource().getClient()
                                                        );

                                                        return 1;
                                                    })
                                    )

                                    .then(
                                            ClientCommandManager.literal("doppel")
                                                    .executes(context -> {

                                                        HorrorManager.fireDoppelganger(
                                                                context.getSource().getClient()
                                                        );

                                                        return 1;
                                                    })
                                    )

                                    .then(
                                            ClientCommandManager.literal("broken")
                                                    .executes(context -> {

                                                        HorrorManager.fireBrokenScript(
                                                                context.getSource().getClient()
                                                        );

                                                        return 1;
                                                    })
                                    )
                    );
                }
        );
    }
}
