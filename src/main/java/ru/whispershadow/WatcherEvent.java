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
                client.player.getName().getString();

        watchers.clear();
    }

    public static boolean isActive() {
        return active;
    }

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

            case 0 -> {

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

            case 1 -> {

                if (ticks >= 50) {

                    ticks = 0;
                    stage = 2;

                    sendMessage(
                            client,
                            "I see you."
                    );
                }
            }

            case 2 -> {

                if (ticks >= 60) {

                    ticks = 0;
                    stage = 3;

                    sendMessage(
                            client,
                            "I know you."
                    );
                }
            }

            case 3 -> {

                if (ticks >= 70) {

                    ticks = 0;
                    stage = 4;

                    sendMessage(
                            client,
                            "Look behind you."
                    );
                }
            }

            case 4 -> {

                if (ticks >= 40) {

                    ticks = 0;
                    stage = 5;

                    spawnFirstWatcher(client);
                }
            }

            case 5 -> {

                if (ticks >= 20) {

                    ticks = 0;

                    moveWatcherIfNeeded(
                            client
                    );
                }
            }

            case 6 -> {

                if (ticks >= 80) {

                    ticks = 0;
                    stage = 7;

                    removeWatchers();
                }
            }

            case 7 -> {

                if (ticks >= 60) {

                    ticks = 0;
                    stage = 8;

                    spawnFinalWatchers(
                            client
                    );
                }
            }

            case 8 -> {

                if (ticks >= 140) {

                    ticks = 0;
                    stage = 9;

                    removeWatchers();
                }
            }

            case 9 -> {

                if (ticks >= 60) {

                    ticks = 0;
                    stage = 10;

                    sendMessage(
                            client,
                            "I was never behind you."
                    );
                }
            }

            case 10 -> {

                if (ticks >= 50) {

                    ticks = 0;
                    stage = 11;

                    sendMessage(
                            client,
                            "It seemed to you..."
                    );
                }
            }

            case 11 -> {

                if (ticks >= 80) {

                    stop(client);
                }
            }
        }
    }

    private static void spawnFirstWatcher(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        Vec3d position =
                getWatcherPosition(
                        client,
                        28.0
                );

        ArmorStandEntity watcher =
                new ArmorStandEntity(
                        EntityType.ARMOR_STAND,
                        client.world
                );

        watcher.setPosition(position);

        watcher.setInvisible(false);

        client.world.addEntity(watcher);

        watchers.add(watcher);
    }

    private static void spawnFinalWatchers(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        int count =
                1 + RANDOM.nextInt(5);

        for (int i = 0; i < count; i++) {

            Vec3d position =
                    getWatcherPosition(
                            client,
                            18.0 +
                                    RANDOM.nextDouble() * 18.0
                    );

            ArmorStandEntity watcher =
                    new ArmorStandEntity(
                            EntityType.ARMOR_STAND,
                            client.world
                    );

            watcher.setPosition(position);

            watcher.setInvisible(false);

            client.world.addEntity(watcher);

            watchers.add(watcher);
        }
    }

    private static void moveWatcherIfNeeded(
            MinecraftClient client
    ) {

        if (watchers.isEmpty())
            return;

        ArmorStandEntity watcher =
                watchers.get(0);

        if (client.player == null)
            return;

        Vec3d playerPos =
                client.player.getEntityPos();

        Vec3d watcherPos =
                watcher.getEntityPos();

        double distance =
                playerPos.distanceTo(
                        watcherPos
                );

        if (distance > 12.0) {

            Vec3d direction =
                    playerPos
                            .subtract(watcherPos)
                            .normalize();

            watcher.setPosition(
                    watcherPos.add(
                            direction.multiply(4.0)
                    )
            );
        }
    }

    private static Vec3d getWatcherPosition(
            MinecraftClient client,
            double distance
    ) {

        ClientPlayerEntity player =
                client.player;

        double angle =
                RANDOM.nextDouble()
                        * Math.PI * 2.0;

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

    private static void removeWatchers() {

        for (ArmorStandEntity watcher :
                watchers) {

            watcher.remove(
                    Entity.RemovalReason.DISCARDED
            );
        }

        watchers.clear();
    }

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
