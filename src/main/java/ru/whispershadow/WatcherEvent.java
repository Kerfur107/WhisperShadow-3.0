package ru.whispershadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WatcherEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;

    private static int stage = 0;
    private static int ticks = 0;

    private static String playerName = null;

    private static final List<ArmorStandEntity> watchers =
            new ArrayList<>();

    private WatcherEvent() {}

    // ========================================
    // START
    // ========================================

    public static void fire(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        if (active)
            return;

        active = true;

        stage = 0;
        ticks = 0;

        playerName =
                client.player
                        .getName()
                        .getString();

        watchers.clear();

        playStatic(client);
    }

    // ========================================
    // STATE
    // ========================================

    public static boolean isActive() {
        return active;
    }

    // ========================================
    // TICK
    // ========================================

    public static void tick(
            MinecraftClient client
    ) {

        if (!active)
            return;

        if (client.player == null ||
                client.world == null) {

            stop(client);
            return;
        }

        ticks++;

        switch (stage) {

            // ========================================
            // SILENCE
            // ========================================

            case 0 -> {

                if (ticks >= 100) {

                    ticks = 0;
                    stage = 1;

                    sendMessage(
                            client,
                            "Hello, " + playerName + "."
                    );
                }
            }

            // ========================================
            // MESSAGE 2
            // ========================================

            case 1 -> {

                if (ticks >= 55) {

                    ticks = 0;
                    stage = 2;

                    sendMessage(
                            client,
                            "I see you."
                    );
                }
            }

            // ========================================
            // MESSAGE 3
            // ========================================

            case 2 -> {

                if (ticks >= 65) {

                    ticks = 0;
                    stage = 3;

                    sendMessage(
                            client,
                            "I know you."
                    );
                }
            }

            // ========================================
            // MESSAGE 4
            // ========================================

            case 3 -> {

                if (ticks >= 80) {

                    ticks = 0;
                    stage = 4;

                    sendMessage(
                            client,
                            "Look behind you."
                    );
                }
            }

            // ========================================
            // FIRST WATCHER
            // ========================================

            case 4 -> {

                if (ticks >= 45) {

                    ticks = 0;
                    stage = 5;

                    spawnFirstWatcher(client);
                }
            }

            // ========================================
            // FIRST WATCHER MOVEMENT
            // ========================================

            case 5 -> {

                if (ticks % 10 == 0) {

                    updateWatcher(client);
                }

                if (ticks >= 240) {

                    ticks = 0;
                    stage = 6;

                    removeWatchers();
                }
            }

            // ========================================
            // FINAL WATCHERS
            // ========================================

            case 6 -> {

                if (ticks >= 70) {

                    ticks = 0;
                    stage = 7;

                    playSting(client);

                    spawnFinalWatchers(client);
                }
            }

            // ========================================
            // FINAL WATCHERS ACTIVE
            // ========================================

            case 7 -> {

                if (ticks >= 150) {

                    ticks = 0;
                    stage = 8;

                    removeWatchers();
                }
            }

            // ========================================
            // FINAL MESSAGE 1
            // ========================================

            case 8 -> {

                if (ticks >= 55) {

                    ticks = 0;
                    stage = 9;

                    sendMessage(
                            client,
                            "I was never behind you."
                    );
                }
            }

            // ========================================
            // FINAL MESSAGE 2
            // ========================================

            case 9 -> {

                if (ticks >= 65) {

                    ticks = 0;
                    stage = 10;

                    sendMessage(
                            client,
                            "It seemed to you..."
                    );
                }
            }

            // ========================================
            // END
            // ========================================

            case 10 -> {

                if (ticks >= 90) {

                    stop(client);
                }
            }
        }
    }

    // ========================================
    // FIRST WATCHER
    // ========================================

    private static void spawnFirstWatcher(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        Vec3d position =
                getWatcherPosition(
                        client,
                        30.0
                );

        ArmorStandEntity watcher =
                new ArmorStandEntity(
                        EntityType.ARMOR_STAND,
                        client.world
                );

        watcher.setPosition(position);
        watcher.setNoGravity(true);
        watcher.setInvisible(false);
        watcher.setShowArms(false);

        client.world.addEntity(watcher);

        watchers.add(watcher);
    }

    // ========================================
    // FINAL WATCHERS
    // ========================================

    private static void spawnFinalWatchers(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        int count =
                1 + RANDOM.nextInt(5);

        for (int i = 0; i < count; i++) {

            double distance =
                    16.0 +
                            RANDOM.nextDouble() *
                                    16.0;

            Vec3d position =
                    getWatcherPosition(
                            client,
                            distance
                    );

            ArmorStandEntity watcher =
                    new ArmorStandEntity(
                            EntityType.ARMOR_STAND,
                            client.world
                    );

            watcher.setPosition(position);
            watcher.setNoGravity(true);
            watcher.setInvisible(false);
            watcher.setShowArms(false);

            client.world.addEntity(watcher);

            watchers.add(watcher);
        }
    }

    // ========================================
    // WATCHER MOVEMENT
    // ========================================

    private static void updateWatcher(
            MinecraftClient client
    ) {

        if (client.player == null ||
                watchers.isEmpty())
            return;

        ArmorStandEntity watcher =
                watchers.get(0);

        if (watcher.isRemoved())
            return;

        Vec3d playerPos =
                client.player.getEntityPos();

        Vec3d watcherPos =
                watcher.getEntityPos();

        double distance =
                playerPos.distanceTo(
                        watcherPos
                );

        if (distance <= 4.5)
            return;

        Vec3d look =
                client.player
                        .getRotationVec(1.0f)
                        .normalize();

        Vec3d toWatcher =
                watcherPos
                        .subtract(playerPos)
                        .normalize();

        double dot =
                look.dotProduct(
                        toWatcher
                );

        boolean lookingAtWatcher =
                dot > 0.45;

        if (lookingAtWatcher)
            return;

        Vec3d direction =
                playerPos
                        .subtract(watcherPos)
                        .normalize();

        double step =
                1.8 +
                        RANDOM.nextDouble() *
                                0.8;

        Vec3d newPosition =
                watcherPos.add(
                        direction.multiply(step)
                );

        watcher.setPosition(
                newPosition
        );
    }

    // ========================================
    // POSITION
    // ========================================

    private static Vec3d getWatcherPosition(
            MinecraftClient client,
            double distance
    ) {

        ClientPlayerEntity player =
                client.player;

        double angle =
                RANDOM.nextDouble() *
                        Math.PI *
                        2.0;

        double x =
                player.getX() +
                        Math.cos(angle) *
                                distance;

        double z =
                player.getZ() +
                        Math.sin(angle) *
                                distance;

        return new Vec3d(
                x,
                player.getY(),
                z
        );
    }

    // ========================================
    // REMOVE WATCHERS
    // ========================================

    private static void removeWatchers() {

        for (ArmorStandEntity watcher :
                watchers) {

            if (!watcher.isRemoved()) {

                watcher.remove(
                        net.minecraft.entity.Entity.RemovalReason.DISCARDED
                );
            }
        }

        watchers.clear();
    }

    // ========================================
    // MESSAGES
    // ========================================

    private static void sendMessage(
            MinecraftClient client,
            String message
    ) {

        if (client.player == null)
            return;

        client.player.sendMessage(
                Text.literal(message)
                        .formatted(
                                Formatting.DARK_GRAY
                        ),
                false
        );
    }

    // ========================================
    // VHS SOUND
    // ========================================

    private static void playStatic(
            MinecraftClient client
    ) {

        if (client.player == null)
            return;

        client.player.playSound(
                ModSounds.WATCHER_STATIC,
                0.18f,
                1.0f
        );
    }

    // ========================================
    // STING SOUND
    // ========================================

    private static void playSting(
            MinecraftClient client
    ) {

        if (client.player == null)
            return;

        client.player.playSound(
                ModSounds.WATCHER_STING,
                0.75f,
                1.0f
        );
    }

    // ========================================
    // STOP SOUNDS
    // ========================================

    private static void stopSounds(
            MinecraftClient client
    ) {

        client.getSoundManager().stopSounds(
                ModSounds.WATCHER_STATIC_ID,
                null
        );

        client.getSoundManager().stopSounds(
                ModSounds.WATCHER_STING_ID,
                null
        );
    }

    // ========================================
    // VHS / CRT OVERLAY
    // ========================================

    public static void renderOverlay(
            DrawContext context
    ) {

        if (!active)
            return;

        int width =
                context.getScaledWindowWidth();

        int height =
                context.getScaledWindowHeight();

        float intensity =
                getEffectIntensity();

        // ========================================
        // DARK SCREEN
        // ========================================

        int darkness =
                (int)
                        (intensity * 18.0f);

        if (darkness > 0) {

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    (darkness << 24)
            );
        }

        // ========================================
        // CRT SCANLINES
        // ========================================

        int lineAlpha =
                (int)
                        (18.0f +
                                intensity *
                                        25.0f);

        int lineColor =
                (lineAlpha << 24);

        int spacing = 3;

        for (
                int y = 0;
                y < height;
                y += spacing
        ) {

            context.fill(
                    0,
                    y,
                    width,
                    y + 1,
                    lineColor
            );
        }

        // ========================================
        // RANDOM VHS DISTORTION
        // ========================================

        if (RANDOM.nextFloat() <
                0.035f +
                        intensity *
                                0.08f) {

            int bandHeight =
                    2 +
                            RANDOM.nextInt(
                                    Math.max(
                                            3,
                                            height / 18
                                    )
                            );

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height -
                                            bandHeight
                            )
                    );

            int alpha =
                    (int)
                            (20 +
                                    intensity *
                                            35);

            context.fill(
                    0,
                    y,
                    width,
                    y + bandHeight,
                    (alpha << 24)
            );
        }

        // ========================================
        // EDGE VIGNETTE
        // ========================================

        int edgeAlpha =
                (int)
                        (10 +
                                intensity *
                                        28);

        int edgeColor =
                (edgeAlpha << 24);

        int edgeSize =
                Math.max(
                        8,
                        (int)
                                (width *
                                        0.025f)
                );

        context.fill(
                0,
                0,
                edgeSize,
                height,
                edgeColor
        );

        context.fill(
                width - edgeSize,
                0,
                width,
                height,
                edgeColor
        );

        context.fill(
                0,
                0,
                width,
                edgeSize,
                edgeColor
        );

        context.fill(
                0,
                height - edgeSize,
                width,
                height,
                edgeColor
        );
    }

    // ========================================
    // EFFECT INTENSITY
    // ========================================

    private static float getEffectIntensity() {

        if (stage <= 3)
            return 0.15f;

        if (stage == 4)
            return 0.25f;

        if (stage == 5)
            return 0.35f;

        if (stage == 6)
            return 0.55f;

        if (stage == 7)
            return 0.75f;

        return 0.45f;
    }

    // ========================================
    // STOP EVENT
    // ========================================

    private static void stop(
            MinecraftClient client
    ) {

        removeWatchers();

        stopSounds(client);

        active = false;

        stage = 0;

        ticks = 0;

        playerName = null;
    }
}
