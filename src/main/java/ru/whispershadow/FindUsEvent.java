package ru.whispershadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public final class FindUsEvent {

    private static final Random RANDOM = new Random();

    private static boolean active = false;

    /*
     * Event timeline:
     *
     * 0 - 35      : FIND US
     * 36 - 75     : F1ND US
     * 76 - 115    : FIND YOU
     * 116 - 155   : glitch
     * 156 - 300   : shadow appears
     * 301 - 360   : WE FOUND YOU
     */
    private static int ticks = 0;

    private static final int TOTAL_DURATION = 360;

    private static ShadowEntity shadow = null;

    private static boolean shadowSpawned = false;
    private static boolean shadowSeen = false;
    private static boolean finalMessageShown = false;

    private static int soundCooldown = 0;

    private static double startX;
    private static double startY;
    private static double startZ;

    private FindUsEvent() {}

    public static boolean isActive() {
        return active;
    }

    public static void fire(MinecraftClient client) {

        if (client.player == null ||
                client.world == null)
            return;

        if (active)
            return;

        if (InsanityManager.getLevelNumber() < 3)
            return;

        /*
         * Don't start while another major chase is running.
         */
        active = true;

        ticks = 0;

        shadow = null;

        shadowSpawned = false;
        shadowSeen = false;
        finalMessageShown = false;

        soundCooldown = 0;

        startX = client.player.getX();
        startY = client.player.getY();
        startZ = client.player.getZ();

        /*
         * First whisper.
         */
        client.player.playSound(
                ModSounds.WHISPER,
                0.12f,
                0.45f
        );

        /*
         * Very small insanity increase.
         */
        InsanityManager.add(
                1.5f + RANDOM.nextFloat() * 2.0f
        );
    }

    public static void tick(MinecraftClient client) {

        if (!active)
            return;

        if (client.player == null ||
                client.world == null) {

            stop(client);
            return;
        }

        ticks++;

        if (soundCooldown > 0)
            soundCooldown--;

        /*
         * PHASE 1
         *
         * FIND US
         */
        if (ticks == 2) {

            client.player.sendMessage(
                    Text.literal("FIND US")
                            .formatted(
                                    Formatting.DARK_RED,
                                    Formatting.BOLD
                            ),
                    false
            );
        }

        /*
         * Small whisper.
         */
        if (ticks == 25) {

            client.player.playSound(
                    ModSounds.WHISPER,
                    0.18f,
                    0.55f
            );
        }

        /*
         * PHASE 2
         *
         * F1ND US
         */
        if (ticks == 40) {

            client.player.sendMessage(
                    Text.literal("F1ND US")
                            .formatted(
                                    Formatting.RED,
                                    Formatting.BOLD
                            ),
                    false
            );

            HorrorManager.fireDirectorGlitch();

            client.player.playSound(
                    ModSounds.GLITCH,
                    0.18f,
                    0.65f
            );
        }

        /*
         * PHASE 3
         *
         * FIND YOU
         */
        if (ticks == 82) {

            client.player.sendMessage(
                    Text.literal("FIND YOU")
                            .formatted(
                                    Formatting.DARK_RED,
                                    Formatting.BOLD
                            ),
                    false
            );

            client.player.playSound(
                    ModSounds.STATIC,
                    0.20f,
                    0.55f
            );

            InsanityManager.add(
                    2.0f + RANDOM.nextFloat() * 2.0f
            );
        }

        /*
         * Short silence.
         *
         * Then the glitch begins.
         */
        if (ticks == 115) {

            HorrorManager.fireDirectorGlitch();

            client.player.playSound(
                    ModSounds.STATIC,
                    0.25f,
                    0.45f
            );
        }

        /*
         * Spawn the figure behind the player.
         */
        if (ticks == 145 &&
                !shadowSpawned) {

            spawnShadow(client);
        }

        /*
         * Shadow behaviour.
         */
        if (shadowSpawned &&
                shadow != null) {

            updateShadow(client);
        }

        /*
         * The figure disappears after some time
         * even if the player never sees it.
         */
        if (ticks == 270 &&
                shadow != null) {

            removeShadow(client);

            client.player.playSound(
                    ModSounds.WHISPER,
                    0.10f,
                    0.42f
            );
        }

        /*
         * Final message.
         */
        if (ticks == 305 &&
                !finalMessageShown) {

            finalMessageShown = true;

            client.player.sendMessage(
                    Text.literal("WE FOUND YOU")
                            .formatted(
                                    Formatting.DARK_RED,
                                    Formatting.BOLD
                            ),
                    false
            );

            client.player.playSound(
                    ModSounds.GLITCH,
                    0.30f,
                    0.50f
            );

            HorrorManager.fireDirectorGlitch();

            InsanityManager.add(
                    3.0f + RANDOM.nextFloat() * 3.0f
            );
        }

        /*
         * Final whisper.
         */
        if (ticks == 335) {

            client.player.playSound(
                    ModSounds.WHISPER,
                    0.18f,
                    0.38f
            );
        }

        /*
         * End.
         */
        if (ticks >= TOTAL_DURATION) {

            stop(client);
        }
    }

    private static void spawnShadow(
            MinecraftClient client
    ) {

        if (client.player == null ||
                client.world == null)
            return;

        /*
         * Spawn behind the player.
         *
         * Distance is intentionally not too far.
         */
        double angle =
                Math.toRadians(
                        client.player.getYaw() + 180.0
                );

        double distance =
                7.0 +
                        RANDOM.nextDouble() * 2.5;

        double x =
                client.player.getX() +
                        Math.sin(angle) * distance;

        double z =
                client.player.getZ() -
                        Math.cos(angle) * distance;

        shadow =
                new ShadowEntity(
                        client.world,
                        client.player.getGameProfile(),
                        false
                );

        /*
         * Negative ID so it cannot collide
         * with real server entities.
         */
        shadow.setId(
                -1200000 -
                        RANDOM.nextInt(100000)
        );

        shadow.refreshPositionAndAngles(
                x,
                client.player.getY(),
                z,
                client.player.getYaw(),
                0.0f
        );

        shadow.setNoGravity(true);
        shadow.setInvisible(false);

        client.world.addEntity(shadow);

        shadowSpawned = true;

        /*
         * Almost silent appearance.
         */
        client.player.playSound(
                ModSounds.WHISPER,
                0.07f,
                0.35f
        );
    }

    private static void updateShadow(
            MinecraftClient client
    ) {

        if (client.player == null ||
                shadow == null)
            return;

        /*
         * Make the figure slowly face the player.
         */
        shadow.lookAt(
        TargetPredicate.DEFAULT,
        client.player.getX(),
        client.player.getY(),
        client.player.getZ()
);

        /*
         * Check whether the player is looking
         * directly at the shadow.
         */
        if (isPlayerLookingAtShadow(client)) {

            if (!shadowSeen) {

                shadowSeen = true;

                client.player.playSound(
                        ModSounds.GLITCH,
                        0.20f,
                        0.55f
                );

                HorrorManager.fireDirectorGlitch();

                /*
                 * It disappears immediately.
                 */
                removeShadow(client);

                /*
                 * Don't immediately reveal the final message.
                 */
                client.player.sendMessage(
                        Text.literal("...")
                                .formatted(
                                        Formatting.DARK_GRAY
                                ),
                        false
                );
            }
        }

        /*
         * If the figure is still alive,
         * keep it slightly behind the player.
         */
        if (shadow != null &&
                !shadow.isRemoved()) {

            double distance =
                    shadow.distanceTo(client.player);

            /*
             * If the player somehow gets too close,
             * make the figure disappear.
             */
            if (distance < 2.0) {

                removeShadow(client);

                client.player.playSound(
                        ModSounds.STATIC,
                        0.18f,
                        0.45f
                );
            }
        }
    }

    private static boolean isPlayerLookingAtShadow(
            MinecraftClient client
    ) {

        if (client.player == null ||
                shadow == null)
            return false;

        Vec3d eye =
                client.player.getCameraPosVec(
                        1.0f
                );

        Vec3d look =
                client.player.getRotationVec(
                        1.0f
                ).normalize();

        Vec3d target =
                shadow.getBoundingBox()
                        .getCenter()
                        .subtract(eye)
                        .normalize();

        double dot =
                look.dotProduct(target);

        /*
         * ~25 degree cone.
         */
        return dot > 0.90 &&
                eye.distanceTo(
                        shadow.getBoundingBox()
                                .getCenter()
                ) < 18.0;
    }

    private static void removeShadow(
            MinecraftClient client
    ) {

        if (shadow != null) {

            shadow.remove(
                    net.minecraft.entity.Entity.RemovalReason
                            .DISCARDED
            );
        }

        shadow = null;
    }

    public static void stop(
            MinecraftClient client
    ) {

        removeShadow(client);

        active = false;

        ticks = 0;

        shadowSpawned = false;
        shadowSeen = false;
        finalMessageShown = false;

        soundCooldown = 0;
    }

    /*
     * ==========================================
     * OVERLAY
     * ==========================================
     */

    public static void renderOverlay(
            DrawContext context,
            RenderTickCounter tickCounter
    ) {

        if (!active)
            return;

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player == null)
            return;

        int width =
                context.getScaledWindowWidth();

        int height =
                context.getScaledWindowHeight();

        /*
         * First phase:
         * almost invisible darkness.
         */
        if (ticks >= 1 &&
                ticks < 40) {

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    0x16000000
            );

            drawCentered(
                    context,
                    "FIND US",
                    width,
                    height,
                    0xB0AA0000,
                    1.0f
            );
        }

        /*
         * Second phase:
         * corrupted text.
         */
        if (ticks >= 40 &&
                ticks < 82) {

            int alpha =
                    130 +
                            RANDOM.nextInt(80);

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    (25 << 24) |
                            0x110000
            );

            drawCentered(
                    context,
                    RANDOM.nextInt(100) < 45
                            ? "F1ND US"
                            : "FIND US",
                    width,
                    height,
                    (alpha << 24) |
                            0xAA0000,
                    1.0f
            );

            renderGlitchLines(
                    context,
                    width,
                    height,
                    5
            );
        }

        /*
         * FIND YOU.
         */
        if (ticks >= 82 &&
                ticks < 145) {

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    0x22000000
            );

            drawCentered(
                    context,
                    "FIND YOU",
                    width,
                    height,
                    0xCC770000,
                    1.0f
            );

            renderGlitchLines(
                    context,
                    width,
                    height,
                    2
            );
        }

        /*
         * Shadow phase.
         *
         * Keep the screen almost normal.
         * This is important for the scare.
         */
        if (ticks >= 145 &&
                ticks < 305) {

            if (RANDOM.nextInt(100) < 12) {

                renderGlitchLines(
                        context,
                        width,
                        height,
                        1
                );
            }
        }

        /*
         * Final message.
         */
        if (ticks >= 300 &&
                ticks < 360) {

            int alpha =
                    170 +
                            RANDOM.nextInt(70);

            context.fill(
                    0,
                    0,
                    width,
                    height,
                    0x18000000
            );

            drawCentered(
                    context,
                    "WE FOUND YOU",
                    width,
                    height,
                    (alpha << 24) |
                            0x990000,
                    1.0f
            );

            if (RANDOM.nextInt(100) < 35) {

                renderGlitchLines(
                        context,
                        width,
                        height,
                        4
                );
            }
        }
    }

    private static void drawCentered(
            DrawContext context,
            String text,
            int width,
            int height,
            int color,
            float scale
    ) {

        var textRenderer =
                MinecraftClient.getInstance()
                        .textRenderer;

        int textWidth =
                textRenderer.getWidth(text);

        int x =
                (width - textWidth) / 2;

        int y =
                height / 2 - 5;

        context.drawText(
                textRenderer,
                Text.literal(text)
                        .formatted(
                                Formatting.BOLD
                        ),
                x,
                y,
                color,
                true
        );
    }

    private static void renderGlitchLines(
            DrawContext context,
            int width,
            int height,
            int amount
    ) {

        for (int i = 0;
             i < amount;
             i++) {

            int y =
                    RANDOM.nextInt(
                            Math.max(
                                    1,
                                    height
                            )
                    );

            int h =
                    1 +
                            RANDOM.nextInt(4);

            int alpha =
                    25 +
                            RANDOM.nextInt(80);

            int color =
                    RANDOM.nextBoolean()
                            ? 0x660000
                            : 0x111111;

            context.fill(
                    0,
                    y,
                    width,
                    Math.min(
                            height,
                            y + h
                    ),
                    (alpha << 24) |
                            color
            );
        }
    }
}
