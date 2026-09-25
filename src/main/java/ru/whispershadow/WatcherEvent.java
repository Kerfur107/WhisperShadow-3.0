package ru.whispershadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
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
    }

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

            // --------------------------------
            // SILENCE
            // --------------------------------

            case 0 -> {

                // 5 секунд полной тишины

                if (ticks >= 100) {

                    ticks = 0;
                    stage = 1;

                    sendMessage(
                            client,
                            "Hello, " +
                                    playerName +
                                    "."
                    );
                }
            }

            // --------------------------------
            // FIRST MESSAGE
            // --------------------------------

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

            // --------------------------------
            // SECOND MESSAGE
            // --------------------------------

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

            // --------------------------------
            // LOOK BEHIND YOU
            // --------------------------------

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

            // --------------------------------
            // WAIT
            // --------------------------------

            case 4 -> {

                if (ticks >= 45) {

                    ticks = 0;
                    stage = 5;

                    spawnFirstWatcher(client);
                }
            }

            // --------------------------------
            // WATCHER
            // --------------------------------

            case 5 -> {

                /*
                 * Фигура существует некоторое время.
                 *
                 * Если игрок смотрит на неё —
                 * она НЕ двигается.
                 *
                 * Если игрок отворачивается —
                 * она приближается.
                 */

                if (ticks % 10 == 0) {

                    updateWatcher(
                            client
                    );
                }

                // Через 12 секунд исчезает

                if (ticks >= 240) {

                    ticks = 0;
                    stage = 6;

                    removeWatchers();
                }
            }

            // --------------------------------
            // SILENCE AFTER FIRST FIGURE
            // --------------------------------

            case 6 -> {

                if (ticks >= 70) {

                    ticks = 0;
                    stage = 7;

                    spawnFinalWatchers(
                            client
                    );
                }
            }

            // --------------------------------
            // FINAL FIGURES
            // --------------------------------

            case 7 -> {

                /*
                 * Последняя сцена.
                 *
                 * От 1 до 5 фигур.
                 * Они просто стоят.
                 */

                if (ticks >= 150) {

                    ticks = 0;
                    stage = 8;

                    removeWatchers();
                }
            }

            // --------------------------------
            // FINAL SILENCE
            // --------------------------------

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

            // --------------------------------
            // FINAL LINE
            // --------------------------------

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

            // --------------------------------
            // END
            // --------------------------------

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
                            RANDOM.nextDouble()
                                    * 16.0;

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

        /*
         * Если фигура уже очень близко —
         * дальше не двигаем.
         */

        if (distance <= 4.5)
            return;

        /*
         * Проверяем направление взгляда игрока.
         */

        Vec3d look =
                client.player.getRotationVec(
                        1.0f
                ).normalize();

        Vec3d toWatcher =
                watcherPos
                        .subtract(playerPos)
                        .normalize();

        double dot =
                look.dotProduct(
                        toWatcher
                );

        /*
         * Чем меньше dot, тем сильнее
         * игрок отвернулся.
         *
         * > 0.45 = игрок смотрит примерно
         * на фигуру.
         */

        boolean lookingAtWatcher =
                dot > 0.45;

        if (lookingAtWatcher)
            return;

        /*
         * Игрок отвернулся.
         *
         * Фигура делает один шаг ближе.
         */

        Vec3d direction =
                playerPos
                        .subtract(watcherPos)
                        .normalize();

        double step =
                1.8 +
                        RANDOM.nextDouble() * 0.8;

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
                RANDOM.nextDouble()
                        * Math.PI
                        * 2.0;

        double x =
                player.getX() +
                        Math.cos(angle)
                                * distance;

        double z =
                player.getZ() +
                        Math.sin(angle)
                                * distance;

        return new Vec3d(
                x,
                player.getY(),
                z
        );
    }

    // ========================================
    // REMOVE
    // ========================================

    private static void removeWatchers() {

        for (ArmorStandEntity watcher :
                watchers) {

            if (!watcher.isRemoved()) {

                watcher.remove(
                        Entity.RemovalReason.DISCARDED
                );
            }
        }

        watchers.clear();
    }

    // ========================================
    // CHAT
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
    // STOP
    // ========================================

    private static void stop(
            MinecraftClient client
    ) {

        removeWatchers();

        active = false;
        stage = 0;
        ticks = 0;

        playerName = null;
    }
}
