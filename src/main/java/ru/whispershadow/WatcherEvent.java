package ru.whispershadow;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class WatcherEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;

    private static int stage = 0;
    private static int ticks = 0;

    private static String playerName = null;

    private static final List<OtherClientPlayerEntity> watchers =
            new ArrayList<>();

    private WatcherEvent() {}

    public static void fire(MinecraftClient client) {

        if (client.player == null || client.world == null)
            return;

        if (active)
            return;

        active = true;
        stage = 0;
        ticks = 0;

        playerName = client.player.getName().getString();

        watchers.clear();

        playStatic(client);
    }

    public static boolean isActive() {
        return active;
    }

    public static void tick(MinecraftClient client) {

        if (!active)
            return;

        if (client.player == null || client.world == null) {
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
                            "Hello, " + playerName + "."
                    );
                }
            }

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

            case 4 -> {

                if (ticks >= 45) {

                    ticks = 0;
                    stage = 5;

                    spawnFirstWatcher(client);
                }
            }

            case 5 -> {

                if (ticks % 5 == 0) {
                    updateWatchers(client);
                }

                if (ticks >= 240) {

                    ticks = 0;
                    stage = 6;

                    removeWatchers();
                }
            }

            case 6 -> {

                if (ticks >= 70) {

                    ticks = 0;
                    stage = 7;

                    playSting(client);

                    spawnFinalWatchers(client);
                }
            }

            case 7 -> {

                if (ticks % 10 == 0) {
                    updateAllWatcherRotations(client);
                }

                if (ticks >= 150) {

                    ticks = 0;
                    stage = 8;

                    removeWatchers();
                }
            }

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

            case 10 -> {

                if (ticks >= 90) {
                    stop(client);
                }
            }
        }
    }

    private static void spawnFirstWatcher(
            MinecraftClient client
    ) {

        if (client.player == null || client.world == null)
            return;

        GameProfile profile =
                getRandomTabProfile(client);

        if (profile == null)
            return;

        Vec3d position =
                getWatcherPosition(
                        client,
                        30.0
                );

        OtherClientPlayerEntity watcher =
                createWatcher(
                        client,
                        profile,
                        position
                );

        if (watcher == null)
            return;

        client.world.addEntity(watcher);

        watchers.add(watcher);

        lookAtPlayer(
                watcher,
                client.player
        );
    }

    private static void spawnFinalWatchers(
            MinecraftClient client
    ) {

        if (client.player == null || client.world == null)
            return;

        List<GameProfile> profiles =
                getTabProfiles(client);

        if (profiles.isEmpty())
            return;

        int count =
                Math.min(
                        1 + RANDOM.nextInt(5),
                        Math.max(1, profiles.size())
                );

        List<GameProfile> shuffled =
                new ArrayList<>(profiles);

        java.util.Collections.shuffle(
                shuffled,
                RANDOM
        );

        for (int i = 0; i < count; i++) {

            GameProfile profile =
                    shuffled.get(
                            i % shuffled.size()
                    );

            double distance =
                    16.0 +
                    RANDOM.nextDouble() * 16.0;

            Vec3d position =
                    getWatcherPosition(
                            client,
                            distance
                    );

            OtherClientPlayerEntity watcher =
                    createWatcher(
                            client,
                            profile,
                            position
                    );

            if (watcher == null)
                continue;

            client.world.addEntity(watcher);

            watchers.add(watcher);

            lookAtPlayer(
                    watcher,
                    client.player
            );
        }
    }

    private static OtherClientPlayerEntity createWatcher(
            MinecraftClient client,
            GameProfile originalProfile,
            Vec3d position
    ) {

        if (client.world == null)
            return null;

        /*
         * Создаём отдельный профиль с новым UUID.
         *
         * Старый вариант с:
         *
         * fakeProfile.properties().putAll(...)
         *
         * больше НЕ используется.
         *
         * Properties у GameProfile здесь immutable,
         * из-за этого раньше происходил UnsupportedOperationException.
         */
        GameProfile fakeProfile =
                new GameProfile(
                        UUID.randomUUID(),
                        originalProfile.name()
                );

        /*
         * Получаем настоящие текстуры выбранного игрока.
         */
        SkinTextures skinTextures =
                getSkinTextures(
                        client,
                        originalProfile
                );

        /*
         * Используем специальную сущность,
         * которая возвращает именно эти текстуры.
         */
        OtherClientPlayerEntity watcher =
                new SkinWatcher(
                        client.world,
                        fakeProfile,
                        skinTextures
                );

        watcher.setPosition(position);

        watcher.setNoGravity(true);
        watcher.setInvulnerable(true);
        watcher.setSilent(true);

        watcher.setCustomNameVisible(false);

        watcher.setId(
                -100000 -
                watchers.size() -
                RANDOM.nextInt(10000)
        );

        lookAtPlayer(
                watcher,
                client.player
        );

        return watcher;
    }

    private static SkinTextures getSkinTextures(
            MinecraftClient client,
            GameProfile profile
    ) {

        /*
         * Если это собственный игрок,
         * используем его уже загруженный скин.
         */
        if (client.player != null
                && client.player.getUuid().equals(profile.id())) {

            return client.player.getSkin();
        }

        /*
         * Ищем игрока в Tab и берём его реальные SkinTextures.
         */
        ClientPlayNetworkHandler networkHandler =
                client.getNetworkHandler();

        if (networkHandler != null) {

            for (PlayerListEntry entry :
                    networkHandler.getPlayerList()) {

                if (entry == null)
                    continue;

                GameProfile entryProfile =
                        entry.getProfile();

                if (entryProfile == null)
                    continue;

                if (entryProfile.id().equals(profile.id())) {

                    return entry.getSkinTextures();
                }
            }
        }

        /*
         * Если настоящий скин не найден,
         * используем стандартный Minecraft skin.
         */
        return DefaultSkinHelper.getSkinTextures(
                profile
        );
    }

    /*
     * Фигура-игрок с принудительно заданным скином.
     *
     * Minecraft будет видеть отдельную сущность
     * с отдельным UUID, но getSkin() будет возвращать
     * SkinTextures настоящего выбранного игрока.
     */
    private static final class SkinWatcher
            extends OtherClientPlayerEntity {

        private final SkinTextures watcherSkin;

        private SkinWatcher(
                net.minecraft.client.world.ClientWorld world,
                GameProfile profile,
                SkinTextures skin
        ) {

            super(
                    world,
                    profile
            );

            this.watcherSkin = skin;
        }

        @Override
        public SkinTextures getSkin() {
            return watcherSkin;
        }
    }

    private static void updateWatchers(
            MinecraftClient client
    ) {

        if (client.player == null)
            return;

        for (OtherClientPlayerEntity watcher :
                new ArrayList<>(watchers)) {

            if (watcher == null || watcher.isRemoved())
                continue;

            lookAtPlayer(
                    watcher,
                    client.player
            );

            Vec3d playerPos =
                    client.player.getEntityPos();

            Vec3d watcherPos =
                    watcher.getEntityPos();

            double distance =
                    playerPos.distanceTo(watcherPos);

            if (distance <= 4.5)
                continue;

            Vec3d look =
                    client.player
                            .getRotationVec(1.0f)
                            .normalize();

            Vec3d toWatcher =
                    watcherPos
                            .subtract(playerPos)
                            .normalize();

            double dot =
                    look.dotProduct(toWatcher);

            boolean lookingAtWatcher =
                    dot > 0.45;

            if (lookingAtWatcher)
                continue;

            Vec3d direction =
                    playerPos
                            .subtract(watcherPos)
                            .normalize();

            double step =
                    0.55 +
                    RANDOM.nextDouble() * 0.35;

            Vec3d newPosition =
                    watcherPos.add(
                            direction.multiply(step)
                    );

            watcher.setPosition(
                    newPosition
            );

            lookAtPlayer(
                    watcher,
                    client.player
            );
        }
    }

    private static void updateAllWatcherRotations(
            MinecraftClient client
    ) {

        if (client.player == null)
            return;

        for (OtherClientPlayerEntity watcher :
                watchers) {

            if (watcher == null || watcher.isRemoved())
                continue;

            lookAtPlayer(
                    watcher,
                    client.player
            );
        }
    }

    private static void lookAtPlayer(
            OtherClientPlayerEntity watcher,
            ClientPlayerEntity player
    ) {

        Vec3d from =
                watcher.getEyePos();

        Vec3d target =
                player.getEyePos();

        double dx =
                target.x - from.x;

        double dy =
                target.y - from.y;

        double dz =
                target.z - from.z;

        double horizontal =
                Math.sqrt(
                        dx * dx +
                        dz * dz
                );

        float yaw =
                (float)
                        Math.toDegrees(
                                Math.atan2(
                                        dz,
                                        dx
                                )
                        ) - 90.0f;

        float pitch =
                (float)
                        -Math.toDegrees(
                                Math.atan2(
                                        dy,
                                        horizontal
                                )
                        );

        watcher.setYaw(yaw);
        watcher.setBodyYaw(yaw);
        watcher.setHeadYaw(yaw);
        watcher.setPitch(pitch);
    }

    private static GameProfile getRandomTabProfile(
            MinecraftClient client
    ) {

        List<GameProfile> profiles =
                getTabProfiles(client);

        if (profiles.isEmpty())
            return null;

        return profiles.get(
                RANDOM.nextInt(
                        profiles.size()
                )
        );
    }

    private static List<GameProfile> getTabProfiles(
            MinecraftClient client
    ) {

        List<GameProfile> profiles =
                new ArrayList<>();

        if (client.player == null)
            return profiles;

        ClientPlayNetworkHandler networkHandler =
                client.getNetworkHandler();

        if (networkHandler != null) {

            Collection<PlayerListEntry> entries =
                    networkHandler.getPlayerList();

            for (PlayerListEntry entry : entries) {

                if (entry == null)
                    continue;

                GameProfile profile =
                        entry.getProfile();

                if (profile == null)
                    continue;

                profiles.add(profile);
            }
        }

        /*
         * Если собственного игрока почему-то нет
         * в Tab, всё равно добавляем его.
         *
         * Благодаря этому /ws watcher работает
         * даже когда игрок находится один.
         */
        GameProfile ownProfile =
                client.player.getGameProfile();

        if (ownProfile != null) {

            boolean alreadyExists = false;

            for (GameProfile profile : profiles) {

                if (profile.id().equals(ownProfile.id())) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                profiles.add(ownProfile);
            }
        }

        return profiles;
    }

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
                player.getX()
                        + Math.cos(angle)
                        * distance;

        double z =
                player.getZ()
                        + Math.sin(angle)
                        * distance;

        return new Vec3d(
                x,
                player.getY(),
                z
        );
    }

    private static void removeWatchers() {

        for (OtherClientPlayerEntity watcher :
                watchers) {

            if (watcher == null)
                continue;

            if (!watcher.isRemoved()) {

                watcher.remove(
                        net.minecraft.entity.Entity.RemovalReason
                                .DISCARDED
                );
            }
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

        int darkness =
                (int)
                        (intensity * 18.0f);

        if (darkness > 0) {

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    darkness << 24
            );
        }

        int lineAlpha =
                (int)
                        (
                                18.0f
                                        + intensity * 25.0f
                        );

        int lineColor =
                lineAlpha << 24;

        int spacing = 3;

        for (int y = 0;
             y < height;
             y += spacing) {

            context.fill(
                    0,
                    y,
                    width,
                    y + 1,
                    lineColor
            );
        }

        if (RANDOM.nextFloat()
                < 0.035f
                + intensity * 0.08f) {

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
                                    height - bandHeight
                            )
                    );

            int alpha =
                    (int)
                            (
                                    20
                                            + intensity * 35
                            );

            context.fill(
                    0,
                    y,
                    width,
                    y + bandHeight,
                    alpha << 24
            );
        }

        int edgeAlpha =
                (int)
                        (
                                10
                                        + intensity * 28
                        );

        int edgeColor =
                edgeAlpha << 24;

        int edgeSize =
                Math.max(
                        8,
                        (int)
                                (width * 0.025f)
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
